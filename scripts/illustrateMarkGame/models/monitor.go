package models

import (
	"bufio"
	"bytes"
	"context"
	"fmt"
	"sync"
	"sync/atomic"
	"time"

	"github.com/gosuri/uilive"
)

var (
	monitorClock = new(sync.Mutex)
	countMinute  = new(int64)
)

type historyRecord struct {
	ID        string
	Mark      int64
	TimeStamp int64
}

// Monitor ..
type Monitor struct {
	ctx        context.Context
	users      []User
	history    []historyRecord
	numHistory int
}

// NewMonitor ..
func NewMonitor(ctx context.Context, users []User, numHistory int) *Monitor {

	history := make([]historyRecord, numHistory)
	return &Monitor{
		ctx:        ctx,
		users:      users,
		history:    history,
		numHistory: numHistory,
	}
}

// Illustrate ..
func (m *Monitor) Illustrate(ctx context.Context) {

	for i := 0; i < len(m.users); i++ {
		go m.users[i].PlayGame(ctx)
	}

}

// MinuteTicker ..
func (m *Monitor) MinuteTicker() {
	go func() {
		for {
			n := time.Now()
			if n.Second() == 0 {

				atomic.AddInt64(countMinute, 1)

				userID := ""
				var mark int64 = 0
				for i := 0; i < len(m.users); i++ {
					current := m.users[i].GetCurrentMark()
					if current > mark {
						userID = m.users[i].ID
						mark = current
					}

					m.users[i].ResetMark()

				}

				// minus because this statemt was late some second compare when start updating
				tmp := n.Add(-time.Second * 1)
				m.updateHistory(historyRecord{
					ID:        userID,
					Mark:      mark,
					TimeStamp: tmp.Unix(),
				})

			}
			time.Sleep(time.Second)
		}
	}()
}

// updateHistory ..
func (m *Monitor) updateHistory(newV historyRecord) {
	monitorClock.Lock()
	m.history = m.history[1:m.numHistory]
	m.history = append(m.history, newV)
	monitorClock.Unlock()
}

// getHistory ..
func (m *Monitor) getHistory() []historyRecord {
	monitorClock.Lock()
	defer monitorClock.Unlock()

	return m.history
}

// Run ..
func (m *Monitor) Run() {

	var b bytes.Buffer
	bufWriter := bufio.NewWriter(&b)

	writer := uilive.New()
	// start listening for updates and render
	writer.Start()

	// Tracking to update history
	m.MinuteTicker()

	for {

		currentHistory := m.getHistory()

		fmt.Fprintf(bufWriter, "List history of winners in %d matches recently:\n", m.numHistory)
		for _, his := range currentHistory {

			dateString := ""
			if his.TimeStamp != 0 {

				tDate := time.Unix(his.TimeStamp, 0)
				dateString = tDate.Format("2006-01-02-15:04")
			} else {
				dateString = "N/A"
			}

			fmt.Fprintf(bufWriter, "At time: %s, UserID: %s win with largest record :%d\n", dateString, his.ID, his.Mark)
		}
		fmt.Fprint(bufWriter, "Tracking live time:\n")

		for i := 0; i < len(m.users); i++ {
			fmt.Fprintf(bufWriter, "UserID: %s, Total mark: %d\n", m.users[i].ID, m.users[i].GetCurrentMark())
		}

		bufWriter.Flush()
		fmt.Fprint(writer, b.String())
		b.Reset()

		time.Sleep(time.Second * 1)
	}

}
