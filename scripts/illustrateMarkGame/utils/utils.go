package utils

import (
	"context"
	"encoding/json"
	"fmt"
	"math/rand"
	"strconv"
	"time"

	"cloud.google.com/go/pubsub"
)

var (
	projectID = "" // notice
	topic     = `` // notice

	ctx         = context.TODO()
	client, err = pubsub.NewClient(ctx, projectID)
	topicClient = client.Topic(topic)
)

func init() {
	rand.Seed(time.Now().UnixNano())
}

// ToJSON ..
func ToJSON(obj interface{}) string {
	var JSONStr string
	b, _ := json.Marshal(obj)
	JSONStr = string(b)

	return JSONStr
}

// PublishMessageToPS ..
func PublishMessageToPS(ctx context.Context, data string, timestamp int64) error {

	t := client.Topic(topic)

	result := t.Publish(ctx, &pubsub.Message{
		Data: []byte(data),
		Attributes: map[string]string{
			"timestamps": strconv.FormatInt(timestamp, 10),
		},
	})

	// The Get method blocks until a server-generated ID or
	// an error is returned for the published message.
	_, err := result.Get(ctx)
	if err != nil {
		return fmt.Errorf(`Error when send message: %s`, err.Error())
	}

	return nil
}

// RandomMark ..
func RandomMark() int64 {
	rand.Seed(time.Now().UnixNano())

	return int64(rand.Intn(5))
}

// RandomTypeGame ..
func RandomTypeGame() int32 {
	rand.Seed(time.Now().UnixNano())

	var (
		typeGames = []int32{1, 2, 3}
	)
	typeGame := typeGames[rand.Intn(len(typeGames))]

	return typeGame
}

// SleepToRandomNexMarkt ..
func SleepToRandomNexMarkt() {
	rand.Seed(time.Now().UnixNano())
	time.Sleep(time.Duration(int64(rand.Intn(10))) * time.Second)
}

// GetTimeStamp ..
func GetTimeStamp() int64 {
	return time.Now().Unix()
}
