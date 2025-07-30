# Nepal Tourism Management System - Simple Version

A streamlined, single-window JavaFX application for managing Nepal tourism operations with secure login.

## 🚀 Quick Start

### Run the Application
\`\`\`bash
# Compile and run
mvn clean compile
mvn javafx:run

# Or run directly with Java
java -cp target/classes com.nepaltourism.simple.SimpleTourismApp
\`\`\`

### 🔐 Login Credentials

The application includes three default user accounts:

| Username | Password | Role | Access Level |
|----------|----------|------|--------------|
| `admin` | `admin123` | Administrator | Full access to all features |
| `manager` | `manager123` | Manager | Full access to tourism operations |
| `staff` | `staff123` | Staff | Basic access to daily operations |

**Default Login:** Use `admin` / `admin123` for full system access.

## ✨ Features

### 🔐 **Secure Login System**
- **User Authentication**: Username/password verification
- **Multiple User Roles**: Admin, Manager, Staff access levels
- **Session Management**: Track login times and user sessions
- **Secure Logout**: Confirmation dialog and data saving
- **User Management**: View all system users and their status

### 🏔️ **Single Window Interface**
- **5 Main Tabs**: Tourists, Guides, Attractions, Bookings, Reports & Users
- **Clean Design**: Intuitive forms and tables
- **Header Bar**: Shows current user and logout option

### 👥 **Tourist Management**
- Add tourists with nationality and emergency contacts
- View all tourists in a table
- Delete selected tourists
- Export to CSV

### 🗺️ **Guide Management**
- Add guides with languages and specialization
- Track experience years
- Manage guide profiles
- Export guide data

### 🏔️ **Attraction Management**
- Add trekking routes and cultural sites
- Set prices and altitude information
- Categorize by type (Trek, Heritage, Adventure, Cultural)
- Export attraction catalog

### 📅 **Booking System**
- Create bookings by selecting tourist, guide, and attraction
- **Automatic Festival Discounts**: 15% off in October/November
- **Safety Warnings**: Altitude alerts for treks above 3000m
- **Emergency Reporting**: Quick emergency contact access
- Export booking records

### 📊 **Reports & User Management**
- **Quick Statistics**: Total counts and revenue
- **Nationality Breakdown**: Tourist distribution by country
- **User Management**: View all system users and login history
- **Current Session Info**: Display current user details
- **CSV Exports**: Individual and combined data exports

## 🎯 **Key Features**

### 🔐 **Security Features**
- **Login Required**: No access without valid credentials
- **Session Tracking**: Monitor user login times
- **Role-Based Access**: Different user roles for different access levels
- **Secure Logout**: Proper session termination
- **Data Protection**: Automatic data saving on logout

### 🎉 **Festival Integration**
- Automatic 15% discount for bookings in October (Dashain) and November (Tihar)
- Festival discount notification popup
- Discount tracking in booking records

### 🛡️ **Safety Features**
- **High Altitude Warning**: Automatic alerts for treks above 3000m
- **Emergency Contacts**: Quick access to Nepal emergency numbers
- **Emergency Reporting**: One-click emergency reporting for bookings

### 💾 **Data Persistence**
- Automatic data saving to `simple_data/` directory
- File-based storage using Java serialization
- User data and login history persistence
- Data loads automatically on startup

## 💡 **How to Use**

1. **Start Application**: Run the main class
2. **Login**: Use default credentials (admin/admin123)
3. **Add Data**: Use the forms in each tab to add records
4. **Create Bookings**: Select tourist, guide, attraction, and travel date
5. **View Reports**: Check the Reports tab for statistics and user management
6. **Export Data**: Use CSV export buttons for external analysis
7. **Logout**: Click logout button in header when finished

## 🔧 **Technical Details**

- **Login System**: Simple file-based user authentication
- **Session Management**: Track current user and login times
- **Data Security**: All data saved securely to local files
- **User Roles**: Support for different access levels
- **Error Handling**: User-friendly alerts and validation

## 📁 **Project Structure**

\`\`\`
src/main/java/com/nepaltourism/simple/
├── SimpleTourismApp.java    # Main application with login
├── Tourist.java             # Tourist data model
├── Guide.java               # Guide data model
├── Attraction.java          # Attraction data model
├── Booking.java             # Booking data model
└── Admin.java               # User/Admin data model
\`\`\`

## 🎨 **UI Highlights**

- **Login Screen**: Clean, professional login interface
- **Nepal Colors**: Red theme inspired by Nepal flag
- **User Header**: Shows current user and role
- **Tabbed Interface**: Easy navigation between modules
- **Secure Logout**: Confirmation dialog and proper session end
- **User Management**: View all system users and their activity

This version provides secure access control while maintaining all the core tourism management functionality in an easy-to-use interface.
