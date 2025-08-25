Bulk Sample Generator

In addition to completing the above assignment, you are tasked with adding a flag to the app that will allow it to bulk generate sample files dynamically for each DataProvider implementation.

## How to Run the Bulk Generator

The app now supports a bulk generation mode that will automatically discover all DataProvider implementations and generate sample JSON files for each one.

### Command Usage

To run the bulk generator, use either of these command-line flags:

```bash
# Using the long flag
./gradlew bootRun --args="--bulk"

# Using the short flag
./gradlew bootRun --args="-b"
```

### What It Does

When you run the app with the `--bulk` or `-b` flag, it will:

1. **Discover all DataProvider implementations** - The app automatically finds all classes that extend `DataProvider` and are annotated with `@Service`
2. **Loop through each provider** - For each discovered provider (e.g., `AnthonyMaysProvider`, `DeanWalstonProvider`, etc.)
3. **Generate sample data** - Creates 10 rows of sample data based on each provider's column type specifications
4. **Save to JSON files** - Saves each provider's sample data to `{providerName}.json` in the `src/main/resources/data/` directory

### Example Output

If the app finds providers like `AnthonyMaysProvider` and `DeanWalstonProvider`, it will generate:
- `anthonymays.json` - Sample data based on AnthonyMaysProvider's column specifications
- `deanwalston.json` - Sample data based on DeanWalstonProvider's column specifications

### Sample Console Output

```
Bulk generating sample files for all DataProvider implementations...
Found 2 DataProvider implementations:
- Generating sample file for: anthonymays
  Column specifications for anthonymays:
    - column1: Integer
    - column2: String
    - column3: Boolean
    - column4: Float
    - column5: Double
    - column6: Long
    - column7: Short
  ✓ Generated: anthonymays.json
- Generating sample file for: deanwalston
  Column specifications for deanwalston:
    - column1: Long
    - column2: Short
    - column3: Double
    - column4: Integer
    - column5: Boolean
    - column6: String
    - column7: Float
  ✓ Generated: deanwalston.json
Bulk generation completed! Files saved to: /path/to/src/main/resources/data
```

### Regular Usage (Single Provider)

The app still supports generating files for individual providers:

```bash
./gradlew bootRun --args="deanwalston"
``` 