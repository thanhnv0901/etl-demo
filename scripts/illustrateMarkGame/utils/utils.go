package utils

import (
	"context"
	"encoding/json"
	"fmt"
	"math/rand"
	"time"

	"cloud.google.com/go/pubsub"
)

var (
	projectID = "knorex-rtb"
	topic     = `staging.demographics_service.user_demographics`

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
func PublishMessageToPS(ctx context.Context, data string) error {

	t := client.Topic(topic)

	result := t.Publish(ctx, &pubsub.Message{
		Data: []byte(data),
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
	return int64(rand.Intn(5))
}

// RandomTypeGame ..
func RandomTypeGame() int32 {

	var (
		typeGames = []int32{1, 2, 3}
	)
	typeGame := typeGames[rand.Intn(len(typeGames))]

	return typeGame
}

// SleepToRandomNexMarkt ..
func SleepToRandomNexMarkt() {
	time.Sleep(time.Duration(int64(rand.Intn(10))) * time.Second)
}
