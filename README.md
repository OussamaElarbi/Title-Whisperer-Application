# Title Whisperer API

Title Whisperer is a project that uses ChatGPT to generate creative titles for various content. It leverages reactive
programming with Spring WebFlux for asynchronous and non-blocking communication.

## Table of Contents

- [Features](#features)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Build and Run](#build-and-run)
- [Configuration](#configuration)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- **Whisper Titles**: Communicates with ChatGPT to generate creative titles.
- **Reactive Design**: Uses Spring WebFlux for asynchronous and reactive communication.
- **Error Handling**: Effectively handles errors during the communication and conversion process.

## Getting Started

These instructions will help you set up and run the Title Whisperer project on your local machine for development and
testing purposes.

### Prerequisites

- Java 11 or higher
- [Maven](https://maven.apache.org/)

### Build and Run

1. **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/title-whisperer.git
    ```

2. **Navigate to the project directory:**

    ```bash
    cd title-whisperer
    ```

3. **Build the project:**

    ```bash
    mvn clean install
    ```

4. **Run the application:**

    ```bash
    java -jar target/title-whisperer-1.0.0.jar
    ```

5. **Access the application at [http://localhost:8080](http://localhost:8080)**

## Configuration

- The application's configuration can be adjusted in the `application.properties` file.

## Usage

To use the Title Whisperer API, follow the guidelines below.

### Required Headers

Include the following headers in your HTTP request:

- **Gpt-Api-Key**: Your ChatGPT API key.

### Sample POST Body

Make a POST request to the endpoint with the content you want to generate titles for. Here's an example using `curl`:

```bash
curl --location 'http://localhost:8080/titles/generate' \
--header 'Gpt-Api-Key: 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' \
--header 'Content-Type: application/json' \
--data '{
    "content": "In 2023, the automotive industry is set to experience a revolutionary transformation. Key trends shaping the future of cars include:\n\n1. Electric Revolution:\n   - Advancements in battery technology lead to extended ranges and faster charging times for electric vehicles (EVs).\n   - Major manufacturers introduce new models with impressive battery life.\n\n2. Autonomous Driving:\n   - Progress in autonomous technology reaches new heights, with Level 4 autonomy becoming a reality."
}'
 ```

## Testing

The project includes unit tests for various components. Run the tests using:

```bash
mvn test