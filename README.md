# Nepal Tourism Management System

A comprehensive JavaFX desktop application for managing tourism operations in Nepal, featuring tourist management, guide coordination, attraction listings, booking systems, festival discounts, and safety protocols.

## Features

### Core Functionality
- **Tourist Management**: Complete CRUD operations for tourist records with emergency contacts
- **Guide Management**: Manage tour guides with language skills, experience, and availability
- **Attraction Management**: Catalog of trekking routes, heritage sites, and cultural attractions
- **Booking System**: Comprehensive booking management with status tracking
- **Festival Integration**: Automatic festival discounts (Dashain, Tihar, etc.)
- **Safety Protocols**: High-altitude warnings, monsoon season restrictions, emergency reporting

### Nepal-Specific Features
- **Multilingual Support**: English and Nepali language interface
- **Festival Discounts**: Automatic application of seasonal discounts during major festivals
- **Safety Alerts**: Altitude sickness warnings for treks above 3,000m
- **Monsoon Restrictions**: Automatic booking restrictions during dangerous weather periods
- **Emergency Management**: Integrated emergency contact system and incident reporting

### Technical Features
- **MVC Architecture**: Clean separation of concerns with Model-View-Controller pattern
- **File-based Persistence**: Data storage using Java serialization (no external database required)
- **Exception Handling**: Comprehensive error handling with user-friendly messages
- **Data Analytics**: Charts and reports for business insights
- **Responsive UI**: JavaFX-based interface with Nepali cultural design elements

## System Requirements

- Java 11 or higher
- JavaFX 17.0.2
- Maven 3.6+
- Minimum 4GB RAM
- 500MB disk space

## Installation & Setup

### Prerequisites
1. Install Java 11 or higher
2. Install Maven
3. Ensure JavaFX is available (included in dependencies)

### Building the Application
\`\`\`bash
# Clone the repository
git clone <repository-url>
cd nepal-tourism-management

# Compile and package
mvn clean compile
mvn package

# Run the application
mvn javafx:run
\`\`\`

### Alternative: Run with Java
\`\`\`bash
# After building
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -cp target/classes com.nepaltourism.TourismApplication
\`\`\`

## Usage

### Default Login Credentials
- **Username**: admin
- **Password**: admin123

### Getting Started
1. Launch the application
2. Log in with default credentials
3. Use the dashboard to navigate to different modules
4. Start by adding tourists, guides, and attractions
5. Create bookings and manage operations

### Key Operations

#### Tourist Management
- Add new tourists with passport and emergency contact details
- Track registration dates and nationality statistics
- Update contact information and travel preferences

#### Guide Management
- Register guides with language skills and specializations
- Track experience levels and availability status
- Manage guide ratings and license information

#### Booking System
- Create bookings linking tourists, guides, and attractions
- Apply automatic festival discounts when applicable
- Monitor booking status and generate confirmations
- Handle emergency reporting for active bookings

#### Safety Features
- Automatic altitude warnings for high-altitude treks
- Monsoon season booking restrictions
- Emergency contact integration
- Safety protocol compliance tracking

## Architecture

### Model Classes
- `Tourist`: Tourist information and emergency contacts
- `Guide`: Guide profiles with skills and availability
- `Attraction`: Tourism destinations with difficulty ratings
- `Booking`: Booking records with status tracking
- `Festival`: Festival periods with discount rates
- `Admin`: System administrator accounts

### Controller Classes
- `LoginController`: Authentication and language selection
- `DashboardController`: Main navigation and statistics
- `TouristController`: Tourist management operations
- `GuideController`: Guide management operations
- `BookingController`: Booking system operations
- `AttractionController`: Attraction catalog management
- `FestivalController`: Festival and discount management
- `SafetyController`: Safety protocols and emergency management
- `ReportController`: Analytics and reporting

### Service Layer
- `DataService`: Centralized data management with file I/O
- `ResourceBundleManager`: Internationalization support
- `SafetyManager`: Safety protocol enforcement
- `FestivalManager`: Festival discount calculations

## Data Storage

The application uses Java object serialization for data persistence:
- `data/tourists.dat`: Tourist records
- `data/guides.dat`: Guide information
- `data/attractions.dat`: Attraction catalog
- `data/bookings.dat`: Booking records
- `data/festivals.dat`: Festival definitions
- `data/admins.dat`: Administrator accounts

## Testing

Run the test suite:
\`\`\`bash
mvn test
\`\`\`

The application includes JUnit 5 tests for core functionality:
- Data service operations
- Model class validation
- Business logic verification

## Internationalization

The application supports English and Nepali languages:
- Resource bundles: `messages_en_US.properties`, `messages_ne_NP.properties`
- Runtime language switching
- Culturally appropriate UI elements

## Safety Features

### High-Altitude Warnings
- Automatic warnings for treks above 3,000m
- Altitude sickness prevention information
- Acclimatization recommendations

### Monsoon Season Restrictions
- Booking restrictions during June-August for dangerous treks
- Weather-related safety alerts
- Alternative attraction suggestions

### Emergency Management
- Emergency contact storage for all tourists
- Incident reporting system
- Integration with Nepal emergency services

## Festival Integration

### Supported Festivals
- **Dashain**: 15% discount (October)
- **Tihar**: 10% discount (November)
- Custom festival periods with configurable discounts

### Automatic Discount Application
- Real-time festival detection
- Automatic price calculation
- Festival notification system

## Reports & Analytics

### Available Reports
- Tourist nationality distribution
- Popular attraction rankings
- Booking trend analysis
- Revenue reports
- Guide performance metrics

### Export Options
- CSV export for external analysis
- Printable report generation
- Chart visualization

## Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Follow Java coding standards
4. Add appropriate tests
5. Submit a pull request

### Code Style
- Follow Oracle Java conventions
- Use meaningful variable names
- Add JavaDoc comments for public methods
- Maintain consistent indentation

## Troubleshooting

### Common Issues

**Application won't start**
- Verify Java 11+ is installed
- Check JavaFX module path
- Ensure all dependencies are available

**Data not persisting**
- Check write permissions in application directory
- Verify data directory exists
- Look for file I/O exceptions in console

**Language switching not working**
- Ensure resource bundles are in classpath
- Check locale configuration
- Verify property file encoding (UTF-8)

### Support
For technical support or bug reports, please create an issue in the project repository.

## License

This project is developed for educational purposes as part of advanced Java programming coursework.

## Acknowledgments

- Nepal Tourism Board for domain expertise
- JavaFX community for UI components
- JUnit team for testing framework
- Maven community for build tools

---

**Nepal Tourism Management System v1.0.0**  
*Developed for Advanced Object-Oriented Programming Course*
