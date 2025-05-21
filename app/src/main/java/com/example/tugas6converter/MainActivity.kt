package com.example.tugas6converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tugas6converter.ui.theme.Tugas6converterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas6converterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConverterApp()
                }
            }
        }
    }
}

@Composable
fun ConverterApp() {
    var selectedConverterIndex by remember { mutableStateOf(0) }
    val converters = listOf("Currency", "Length", "Speed")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Converter App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Converter selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            converters.forEachIndexed { index, title ->
                FilterChip(
                    selected = selectedConverterIndex == index,
                    onClick = { selectedConverterIndex = index },
                    label = { Text(title) }
                )
            }
        }

        when (selectedConverterIndex) {
            0 -> CurrencyConverter()
            1 -> LengthConverter()
            2 -> SpeedConverter()
        }
    }
}

@Composable
fun CurrencyConverter() {
    val currencies = listOf("IDR", "USD", "EUR", "JPY")
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf(currencies[0]) }
    var toCurrency by remember { mutableStateOf(currencies[1]) }
    var result by remember { mutableStateOf("") }
    
    // Currency conversion rates (base: IDR)
    val conversionRates = mapOf(
        "IDR" to 1.0,      // 1 IDR = 1 IDR
        "USD" to 0.000064, // 1 IDR ≈ 0.000064 USD
        "EUR" to 0.000059, // 1 IDR ≈ 0.000059 EUR
        "JPY" to 0.0097    // 1 IDR ≈ 0.0097 JPY
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Currency Converter",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("From", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currencies.size) { index ->
                    val currency = currencies[index]
                    FilterChip(
                        selected = fromCurrency == currency,
                        onClick = { fromCurrency = currency },
                        label = { Text(currency) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("To", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currencies.size) { index ->
                    val currency = currencies[index]
                    FilterChip(
                        selected = toCurrency == currency,
                        onClick = { toCurrency = currency },
                        label = { Text(currency) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (amount.isNotEmpty()) {
                    val inputAmount = amount.toDoubleOrNull() ?: 0.0
                    val fromRate = conversionRates[fromCurrency] ?: 1.0
                    val toRate = conversionRates[toCurrency] ?: 1.0
                    
                    // Convert to IDR first, then to target currency
                    val resultValue = if (fromCurrency == "IDR") {
                        inputAmount * toRate
                    } else if (toCurrency == "IDR") {
                        inputAmount / fromRate
                    } else {
                        (inputAmount / fromRate) * toRate
                    }
                    
                    result = "%.4f".format(resultValue)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Text(
                    text = "$amount $fromCurrency = $result $toCurrency",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun LengthConverter() {
    val lengthUnits = listOf("mm", "cm", "m", "km")
    var value by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(lengthUnits[0]) }
    var toUnit by remember { mutableStateOf(lengthUnits[2]) }
    var result by remember { mutableStateOf("") }
    
    // Conversion to base unit (mm)
    val toBaseUnit = mapOf(
        "mm" to 1.0,
        "cm" to 10.0,
        "m" to 1000.0,
        "km" to 1000000.0
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Length Converter",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("From", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lengthUnits.size) { index ->
                    val unit = lengthUnits[index]
                    FilterChip(
                        selected = fromUnit == unit,
                        onClick = { fromUnit = unit },
                        label = { Text(unit) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("To", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lengthUnits.size) { index ->
                    val unit = lengthUnits[index]
                    FilterChip(
                        selected = toUnit == unit,
                        onClick = { toUnit = unit },
                        label = { Text(unit) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (value.isNotEmpty()) {
                    val inputValue = value.toDoubleOrNull() ?: 0.0
                    val fromFactor = toBaseUnit[fromUnit] ?: 1.0
                    val toFactor = toBaseUnit[toUnit] ?: 1.0
                    
                    // Convert to base unit (mm) then to target unit
                    val resultValue = (inputValue * fromFactor) / toFactor
                    result = if (resultValue >= 1000 || resultValue < 0.01) {
                        "%.6e".format(resultValue)
                    } else {
                        "%.4f".format(resultValue)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Text(
                    text = "$value $fromUnit = $result $toUnit",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SpeedConverter() {
    val speedUnits = listOf("km/h", "mp/h")
    var value by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(speedUnits[0]) }
    var toUnit by remember { mutableStateOf(speedUnits[1]) }
    var result by remember { mutableStateOf("") }
    
    // Conversion factor (km/h to mp/h)
    val kmhToMph = 0.621371

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Speed Converter",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = value,
            onValueChange = { value = it },
            label = { Text("Value") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("From", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(speedUnits.size) { index ->
                    val unit = speedUnits[index]
                    FilterChip(
                        selected = fromUnit == unit,
                        onClick = { fromUnit = unit },
                        label = { Text(unit) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("To", style = MaterialTheme.typography.bodyMedium)
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(speedUnits.size) { index ->
                    val unit = speedUnits[index]
                    FilterChip(
                        selected = toUnit == unit,
                        onClick = { toUnit = unit },
                        label = { Text(unit) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (value.isNotEmpty()) {
                    val inputValue = value.toDoubleOrNull() ?: 0.0
                    val resultValue = when {
                        fromUnit == "km/h" && toUnit == "mp/h" -> inputValue * kmhToMph
                        fromUnit == "mp/h" && toUnit == "km/h" -> inputValue / kmhToMph
                        else -> inputValue // Same unit conversion
                    }
                    
                    result = "%.2f".format(resultValue)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            ) {
                Text(
                    text = "$value $fromUnit = $result $toUnit",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}