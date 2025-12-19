# Java Conferences MCP Server

A Java Spring Boot-based Model Context Protocol (MCP) server that provides tools to query Java conferences from [javaconferences.org](https://javaconferences.org).

## Features

- Query Java conferences (past and upcoming)
- Filter upcoming conferences
- Find conferences with open Call for Papers (CFP)
- Search conferences within a specific date range

## Installation

### Using JBang (Recommended)

Configure in your MCP client (e.g., Cursor, Claude Desktop):

```json
{
  "mcpServers": {
    "javaconf": {
      "command": "jbang",
      "args": ["https://github.com/bmvermeer/spring-mcp-javaconf/releases/latest/download/javaconfmcp.jar"]
    }
  }
}
```

### Using Java JAR

If you prefer to build and run from a JAR:

```bash
./mvnw clean package
```

Then configure:

```json
{
  "mcpServers": {
    "javaconf": {
      "command": "java",
      "args": ["-jar", "/path/to/javaconfmcp.jar"]
    }
  }
}
```

## Available Tools

- **`javaConf_get_allEvents`**: Retrieve all Java conferences (past and upcoming)
- **`javaConf_get_upcommingEvents`**: Fetch upcoming conferences
- **`javaConf_get_openCfps`**: List conferences with open Call for Papers
- **`javaConf_get_eventsInTimeframe`**: Get conferences within a date range (ISO-8601 LocalDateTime)

## Requirements

- Java 21 or higher
- JBang (for JBang installation method)
