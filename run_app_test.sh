#!/bin/bash

export JOB_ENV="test"
export GOOGLE_APPLICATION_CREDENTIALS="src/main/resources/knorex-rtb-fba740906c31-test.json"

gradle clean run  --args="--environment=$JOB_ENV"