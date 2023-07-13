# Inventory Management System

This project is an Inventory Management System designed to manage inventory items within a company. It consists of a desktop application developed in Java for validating the current states of the items, a mobile application developed in Android Java for identifying items using barcode scanning, a backend REST API developed in Python with Flask, and a MySQL database for data storage.

## Desktop Application

The desktop application provides processes for validating the current states of the inventory items. It allows users to perform various operations, such as checking the availability, condition, and location of items.

## Mobile Application

The mobile application, developed in Android Java, enables users to identify items by scanning their barcodes using the device's camera. Once an item is identified, users can provide observations on its current state, including condition, damage, or any other relevant information.

## Backend REST API

The backend REST API, developed in Python with Flask, serves as the central component of the Inventory Management System. It handles data storage, retrieval, and manipulation related to inventory items. The API allows the desktop and mobile applications to communicate and exchange data seamlessly.

## MySQL Database

The MySQL database is used for storing and managing the inventory data. It provides a reliable and scalable solution for handling large amounts of data related to inventory items, including their current states, availability, and location.

## Prerequisites

To run this project, ensure you have the following prerequisites installed on your system:

- Java Development Kit (JDK)
- Android Studio (for running the mobile application)
- Python
- Flask
- MySQL

## Getting Started

To use the Inventory Management System, follow the instructions below:

1. Clone this repository to your local machine.
2. Set up and run the MySQL database on your system.
3. Set up and run the backend REST API by following the instructions provided in the backend directory.
4. Open the desktop application in an IDE, compile and run it using the Java Development Kit.
5. Open the mobile application in Android Studio, build and run it on an Android device or emulator.

## Usage

Once the Inventory Management System is set up and running, you can utilize the following features:

- Desktop Application:
  - Validate the current states of inventory items.
  - Check availability, condition, and location of items.

- Mobile Application:
  - Scan item barcodes to identify them.
  - Provide observations on the current state of items.

- Backend REST API:
  - Handle data storage, retrieval, and manipulation related to inventory items.
  - Enable communication between the desktop and mobile applications.

## Technologies Used

The Inventory Management System is developed using the following technologies:

- Java: The programming language used for the desktop application.
- Android Java: The programming language used for the mobile application.
- Python: The programming language used for the backend REST API.
- Flask: A micro web framework for building RESTful APIs with Python.
- MySQL: An open-source relational database management system.
