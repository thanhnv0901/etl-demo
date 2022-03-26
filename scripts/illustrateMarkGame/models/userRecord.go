package models

// UserRecord ..
type UserRecord struct {
	ID        string `json:"id"`
	Mark      int64  `json:"mark"`
	TypeGame  int    `json:"type_game"`
	TimeStamp int64  `json:"timestamp"`
}
