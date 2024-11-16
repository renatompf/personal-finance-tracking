
# Personal Finance Tracking

-------------------------

## Project Description

This project is crafted in [Kotlin](https://kotlinlang.org/), leveraging the [Spring Boot Framework](https://spring.io/projects/spring-boot/), and using [PostgreSQL](https://www.postgresql.org/) as the primary database. Additionally, [TimescaleDB](https://www.timescale.com/) is used for optimized time-series data management, facilitating efficient storage and querying of monthly statistics. The application also integrates Spring's scheduling capabilities for automated tasks such as periodic data updates and statistical aggregations.

----------------------

## API Endpoints Overview:

### Authentication Endpoints

#### Register
- **Endpoint:** `POST /auth/register`
- **Description:** Registers a new user.
- **Request Body:** `RegistrationRequest`
- **Response:** `AuthenticationResponse`
- **HTTP Response Code:** 201 (Created)

**Example:**
```http
POST /auth/register
Content-Type: application/json

{
    "name": "Renato",
    "email": "renato@github.com",
    "password": "password"
}
```

#### Login
- **Endpoint:** `POST /auth/login`
- **Description:** Authenticates a user.
- **Request Body:** `AuthenticationRequest`
- **Response:** `AuthenticationResponse`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
POST /auth/login
Content-Type: application/json

{
    "email": "renato@github.com",
    "password": "password"
}
```

### Budget Endpoints

#### Get Remaining Budget
- **Endpoint:** `GET /budgets/remaining`
- **Description:** Retrieves the remaining budget.
- **Response:** `RemainingBudgetDto`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
GET /budgets/remaining
```

### Settings Endpoints

#### Get Settings
- **Endpoint:** `GET /settings`
- **Description:** Retrieves user settings.
- **Response:** `SettingsDtoOut`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
GET /settings
```

#### Add Settings
- **Endpoint:** `POST /settings`
- **Description:** Adds new settings.
- **Request Body:** `CreatingSettingsDto`
- **Response:** `SettingsDtoOut`
- **HTTP Response Code:** 204 (No Content)

**Example:**
```http
POST /settings
Content-Type: application/json

{
  "monthlyBudgetValue": 5000
}
```

#### Update Settings
- **Endpoint:** `PUT /settings`
- **Description:** Updates user settings.
- **Request Body:** `CreatingSettingsDto`
- **Response:** `SettingsDtoOut`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
PUT /settings
Content-Type: application/json

{
  "monthlyBudgetValue": 6000
}
```

### Transaction Endpoints

#### Get All Transactions
- **Endpoint:** `GET /transactions`
- **Description:** Retrieves a paginated list of all transactions.
- **Parameters:** `Pageable` (for pagination)
- **Response:** Paginated list of `TransactionOutDto`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
GET /transactions?page=0&size=10
```

#### Get Transaction by ID
- **Endpoint:** `GET /transactions/{transactionId}`
- **Description:** Retrieves a transaction by its ID.
- **Path Variable:** `transactionId` (Transaction ID)
- **Response:** `TransactionOutDto`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
GET /transactions/<UUID>
```

#### Add Transaction
- **Endpoint:** `POST /transactions`
- **Description:** Creates a new transaction.
- **Request Body:** `TransactionInDto`
- **Response:** `TransactionOutDto`
- **HTTP Response Code:** 204 (No Content)

**Example:**
```http
POST /transactions
Content-Type: application/json

{
  "amount": -150.50,
  "category": "ENTERTAINMENT",
  "description": "Nintendo Switch Black Friday"
}
```

#### Delete Transaction by ID
- **Endpoint:** `DELETE /transactions/{transactionId}`
- **Description:** Deletes a transaction by its ID.
- **Path Variable:** `transactionId` (Transaction ID)
- **Response:** `Void`
- **HTTP Response Code:** 204 (No Content)

**Example:**
```http
DELETE /transactions/<UUID>
```

#### Get Monthly Summary
- **Endpoint:** `GET /transactions/summary/monthly`
- **Description:** Retrieves a monthly summary of transactions.
- **Parameters:** `category` (optional)
- **Response:** List of `TransactionMonthlySummaryDto`
- **HTTP Response Code:** 200 (OK)

**Example:**
```http
GET /transactions/summary/monthly?category=FOOD
```
