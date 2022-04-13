#!/bin/bash

export GOOGLE_APPLICATION_CREDENTIALS="knorex-rtb-fba740906c31-test.json"

MallocNanoZone=0 go run -race main.go
