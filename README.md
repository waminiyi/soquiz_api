# SoQuiz API

Welcome to the **SoQuiz Rest API**!
This API is built using the [Ktor](https://ktor.io) framework,
with PostgreSQL and H2 databases for persistence, Exposed ORM 
for database interactions, and JWT for authentication.

## Table of Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Install and Run](#install-and-run)
- [API Endpoints](#api-endpoints)
- [License](#license)

## Getting Started

This API allows you to perform CRUD operations and authenticate users using JWT.
It’s designed to be lightweight, secure, and easy to integrate.

### Prerequisites

Before you can run the API, make sure you have the following installed:

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk20-archive-downloads.html)
- [Gradle 8.5 or higher](https://gradle.org/install/)
- [Set up the PostgreSQL database](https://www.postgresql.org/download/)
- [Docker](https://www.docker.com/) (optional, if you use Docker for PostgreSQL)

## Install and Run

1. Clone this repository
2. Add the following to your environment:
   - POSTGRES_USER=your_postgres_user
   - POSTGRES_PASSWORD=your_postgres_password
   - POSTGRES_URL=jdbc:postgresql://localhost:5432/your_db_name
   - JWT_SECRET=your_jwt_secret
3. Build and run

## API endpoints
The api documentation can be found at https://www.soquiz.sotopatrick.com/swagger


## 💙 **Find this repository useful?**  
Support it by starring and sharing it ⭐


## License
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
