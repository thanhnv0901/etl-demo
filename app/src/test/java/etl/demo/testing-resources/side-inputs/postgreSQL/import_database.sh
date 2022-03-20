#!/usr/bin/env bash
/wait-for-it.sh functional_api_test_postgresdb:5432
createdb -h functional_api_test_postgresdb -U postgres knx_demographic
psql -h functional_api_test_postgresdb -d knx_demographic -U postgres -f /demographic_information_test.sql
