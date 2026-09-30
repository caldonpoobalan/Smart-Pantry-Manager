# Smart Pantry Manager

A native Android mobile application designed to help users manage their household pantry inventory, track food expiry dates, reduce food waste, and discover recipes they can cook immediately with the ingredients they already have on hand.

**Author:** Caldon Poobalan  
**Student Number:** 402306580  
**Module:** Mobile App Development 700 (BSc IT)  

---

## Features

### 1. Pantry Stock Inventory
- **Add, Edit, and Delete Ingredients:** Log ingredient names, quantities, units, and optional expiry dates.
- **Live Search Filtering:** Real-time search bar at the top of the pantry screen to quickly find specific items.
- **Expiry Tracking & Visual Alerts:**
  - Standard / Fresh: More than 5 days remaining.
  - Orange Warning: Expiring within 5 days.
  - Red Alert: Expired items.
  - Gray Notice: No expiry date specified.

### 2. Strict Recipe Matching Engine
- **Zero-Waste Suggestions:** A recipe is suggested only when the user possesses 100% of the required non-staple ingredients with sufficient quantity (Stock Quantity >= Required Quantity).
- **Kitchen Staples:** Basic staples (`salt`, `black pepper`, `water`, `sugar`) are assumed to be readily available in every household and do not block recipe suggestions.
- **Flexible Ingredient Matching:** Matches plurals and multi-word ingredients (for example, "eggs" matches "egg", "cloves garlic" matches "garlic").

### 3. Unit Conversion & Piece Estimation
- Seamless conversion across Metric and Imperial measurement systems:
  - Weight: Grams (g), Kilograms (kg), Ounces (oz), Pounds (lb).
  - Volume: Milliliters (ml), Liters (L), Fluid Ounces (fl oz), Gallons (gal).
  - Piece Estimation: Converts countable items (`pcs`) to grams for cooking calculations based on standard portion weights (eggs, potatoes, onions, tomatoes, chicken breasts).
- Fractional inputs retain decimal fidelity without premature whole-number rounding.

### 4. "Almost There" Screen (Bonus Stretch Feature)
- A separate, dedicated screen accessed from the Suggested Recipes tab.
- Displays recipes where the user is missing **exactly one** required ingredient.
- Clearly displays what ingredient is missing and the exact quantity needed, completely separated from strict suggestions to comply with the project rubric.
- View-only recipe cards keep user focus on acquiring the missing ingredient.

### 5. Settings & User Preferences
- **Theme Modes:** Supports system default, Light Theme, and Dark Theme.
- **Unit System Preference:** Toggle between Metric and Imperial display modes.
- **Expiry Notification Alerts:** Switch alerts on or off.
- **Reset Pantry:** Option to clear all inventory stock while safely preserving the recipe database.

---

## Recipe Catalog

The app includes 18 built-in seed recipes:
1. Baked Tomato and Cheese
2. Cream Potato Fries
3. Devil Eggs
4. Classic Scrambled Eggs
5. Garlic Butter Pasta
6. Cheesy Garlic Bread
7. Tomato Egg Stir Fry
8. Simple Fried Rice
9. Crispy Potato Wedges
10. Creamy Tomato Soup
11. Onion Omelette
12. Garlic Chicken Skillet
13. Classic French Toast
14. Macaroni and Cheese
15. Pan-Seared Chicken and Potatoes
16. Egg Salad Sandwich
17. Sauteed Garlic Potatoes
18. Chicken Fried Rice

---

## Technical Stack & Architecture

- **Language:** Java
- **Target SDK:** Android 14 / 15 (API 34-37)
- **Minimum SDK:** Android 7.0 (API 24)
- **Local Persistence:** Room Database (SQLite) with relational foreign keys
- **UI Components:** Android Material Components (MaterialButton, TextInputLayout, BottomNavigationView, CardView, RecyclerView)
- **Architecture Pattern:** Clean separation of concerns:
  - `activity` - Main UI Activities (`HubHomeAct`, `IngredFormAct`, `RecpSpecAct`, `AlmostThereAct`)
  - `fragment` - Bottom navigation tab screens (`StockVwFrag`, `MatchRecpFrag`, `PrefCfgFrag`)
  - `adapter` - RecyclerView list adapters (`StockItemAdapt`, `RecpCardAdapt`, `AlmostRecpAdapt`)
  - `database` - Room Database instance and seed data (`PantryRoomDb`, `RecpSeedData`)
  - `dao` - SQLite Data Access Objects (`StockDataAcc`, `RecpDataAcc`, `RecpIngredDataAcc`)
  - `model` - Database Entities and POJOs (`StockEntity`, `RecpEntity`, `RecpIngredEntity`, `AlmostRecpItem`)
  - `logic` - Pure Java business logic (`UnitConvertLogic`, `IngredParseLogic`, `RecpMatchLogic`)

---

## Getting Started & Running the Project

### Prerequisites
- Android Studio (Jellyfish, Koala, Ladybug, or newer)
- JDK 17 or JDK 21
- Android Virtual Device (AVD) or physical device running API 24 or higher

### Steps to Run
1. Clone this repository:
   ```bash
   git clone https://github.com/caldonpoobalan/Smart-Pantry-Manager.git
   ```
2. Open Android Studio and select **Open**, then choose the `Smart_Pantry_Manager` directory.
3. Allow Gradle to download dependencies and sync the project.
4. Select your preferred emulator or physical device from the device dropdown.
5. Click the green **Run (Shift + F10)** button to compile, install, and launch the app.
