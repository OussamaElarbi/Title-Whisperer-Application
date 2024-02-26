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
- [License](#license)

## Features

- **Whisper Titles**: Communicates with ChatGPT to generate creative titles.
- **Reactive Design**: Uses Spring WebFlux for asynchronous and reactive communication.
- **Error Handling**: Effectively handles errors during the communication and conversion process.

## Getting Started

These instructions will help you set up and run the Title Whisperer project on your local machine for development and
testing purposes.

### Prerequisites

- Java 17 or higher
- [Maven](https://maven.apache.org/)
- IDE (like [Intellij IDEA](https://www.jetbrains.com/idea/download))
- ChatGPT API Key - [ChatGPT API](https://chat.openai.com/docs/api-key)

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
    java -jar target/TitleWhisperer-1.0.jar 
    ```

5. **Access the application at [http://localhost:8080/titles/generate](http://localhost:8080/titles/generate) - Use [Postman Application](https://www.postman.com/downloads/) for best experience**

## Configuration

- The application's configuration can be adjusted in the `application.properties` file.

## Usage

To use the Title Whisperer API, follow the guidelines below.

### Required Headers

Include the following headers in your HTTP request:

- **Gpt-Api-Key**: Your ChatGPT API key

### Required Body (JSON)
- `{"content": "Your content"}`

### Sample API Request

Make a POST request to the endpoint with the content you want to generate titles for. Here's an example using `curl`:

```bash
curl --location 'http://localhost:8080/titles/generate' \
--header 'Gpt-Api-Key: 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' \
--header 'Content-Type: application/json' \
--data '{
    "content": "In 2023, the automotive industry is set to experience a revolutionary transformation. Key trends shaping the future of cars include:\n\n1. Electric Revolution:\n   - Advancements in battery technology lead to extended ranges and faster charging times for electric vehicles (EVs).\n   - Major manufacturers introduce new models with impressive battery life.\n\n2. Autonomous Driving:\n   - Progress in autonomous technology reaches new heights, with Level 4 autonomy becoming a reality."
}'
 ```

### Sample API Response
<pre>
{
  "generated_titles": [
    "Revolutionizing the Road: Key Trends Shaping the Future of Cars in 2023",
    "Unleashing the Power: The Electric Revolution in the Automotive Industry",
    "Driving Towards Tomorrow: Advances in Electric Vehicles in 2023",
    "Embracing Autonomy: The Future of Self-Driving Cars in 2023",
    "The Road Ahead: Key Transformations in the Automotive Industry",
    "Electrifying the Streets: The Rise of Electric Vehicles in 2023",
    "Autonomous Revolution: Unveiling the Future of Driving",
    "Revolutionary Milestones: The Progression of Autonomous Technology in 2023",
    "Accelerating Change: Key Trends Reshaping the Automotive Industry",
    "Innovations on Wheels: The Evolution of Cars in 2023"
  ]
}
</pre>

## Postman 

You can use Postman to test the API seamlessly

**Method 1:**

**Copy past the [curl](#Sample-API-Request) command, and import it to Postman.**

![Picture 1](screenshots/screenshot_2.png)

**Method 2:**

**Create new request, and set up the url and Gpt-Api-Key header**

![Picture 2](screenshots/screenshot_1.png)

**Past the content in the body section as JSON**

![Picture 3](screenshots/screenshot_3.png)



## Testing

The project includes unit tests for various components. Run the tests using:

```bash
mvn test
```

Feedback and Support
--------------------

For any questions or feedback, please contact me at <ayade98@gmail.com>.


## License

This project is licensed under the terms of the MIT License. See the [LICENSE](https://github.com/OussamaElarbi/Title-Whisperer-Application/blob/ecc05a93a4109a91149f0520f2b4626fa6dc6746/LICENSE.txt) file for details.

* * * * *