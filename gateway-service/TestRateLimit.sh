#!/bin/bash

TOKEN="eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyLXR5cGUiOiJBZG1pbiIsInN1YiI6Im9tYW1lYXp5QGdtYWlsLmNvbSIsInVzZXItaWQiOiI0OGUyYWE4MS0zNzVhLTQ0N2UtYjRlMy01YzEwMjFkODI2NDciLCJyb2xlcyI6IlJPTEVfQ0FOX0RFTEVURV9ST0xFLFJPTEVfQ0FOX1ZJRVdfVVNFUlMsUk9MRV9DQU5fVVBEQVRFX1JPTEUsUk9MRV9DQU5fQUREX1JPTEUsUk9MRV9DQU5fREVMRVRFX1VTRVJTIiwibmFtZSI6IklzYWlhaCIsImV4cCI6MTc1NjE0MzI3NiwiaWF0IjoxNzU2MDU2ODc2LCJlbWFpbCI6Im9tYW1lYXp5QGdtYWlsLmNvbSIsImlzc3VlciI6IklzYWlhaCBPbWFtZSJ9.rUse-Uxm7IO43ZI8vDnPJALFXqSJgw0sn7d1eurNje8Uhz1HYgjZcgzCdXtYT4SEgttF8Euu0UN_8AS8xkcPsQ"
URL="http://localhost:8081/api/v1/users/management/find-all"

echo "Testing rate limiter with 105 requests..."

for i in {1..105}; do
  response=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" $URL)
  echo "Request $i: HTTP $response"

  if [ "$response" -eq 429 ]; then
    echo "RATE LIMITED at request $i!"
    break
  fi

  # Small delay to avoid overwhelming
  sleep 0.01
done
