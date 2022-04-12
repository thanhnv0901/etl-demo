package models

import (
	"context"
	"illustrateMarkGame/utils"
	"io"
	"sync"
)

// User ..
type User struct {
	ID                  string
	totalScorePerMinute int64
	UserWritter         io.Writer
	WLock               sync.Mutex
}

// AddMark ..
func (u *User) AddMark(mark int64) {
	u.WLock.Lock()
	defer u.WLock.Unlock()
	u.totalScorePerMinute += int64(mark)
}

// GetCurrentMark ..
func (u *User) GetCurrentMark() int64 {
	u.WLock.Lock()
	defer u.WLock.Unlock()
	return u.totalScorePerMinute
}

// ResetMark ..
func (u *User) ResetMark() {
	u.WLock.Lock()
	defer u.WLock.Unlock()
	u.totalScorePerMinute = 0
}

// PlayGame ..
func (u *User) PlayGame(ctx context.Context) {

	// u.Witter.Newline()
	var mark int64 = 0
	for {

		mark = utils.RandomMark()
		u.AddMark(mark)

		var record = UserRecord{
			ID:        u.ID,
			Mark:      mark,
			TypeGame:  0, // notice no yet update
			TimeStamp: utils.GetTimeStamp(),
		}

		message := utils.ToJSON(record)
		utils.PublishMessageToPS(ctx, message, record.TimeStamp)

		utils.SleepToRandomNexMarkt()

	}
}
