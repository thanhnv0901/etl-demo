package main

import (
	"context"
	"illustrateMarkGame/models"
)

func main() {

	var (
		ctx     = context.Background()
		monitor *models.Monitor
	)

	var (
		userIDs    = []string{"001", "002", "003", "004", "005"}
		users      = make([]models.User, len(userIDs))
		numHistory = 10
	)

	// Set id for users
	for i := 0; i < len(userIDs); i++ {
		users[i] = models.User{
			ID: userIDs[i],
		}
	}

	monitor = models.NewMonitor(ctx, users, numHistory)
	monitor.Illustrate(ctx)

	go monitor.Run()

	c := make(chan int)
	c <- 1

}
