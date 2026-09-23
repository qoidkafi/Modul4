package com.example.halamanpemesanan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PemesananTiketRoot()
        }
    }
}

@Composable
fun PemesananTiketRoot() {
    var hargaTiket by remember { mutableIntStateOf(75000) }
    var jumlahTiket by remember { mutableIntStateOf(1) }
    var namaPembeli by remember { mutableStateOf("") }
    
    var statusMessage by remember { mutableStateOf("Nama Masih Kosong") }
    var isProcessing by remember { mutableStateOf(false) }

     LaunchedEffect(namaPembeli, isProcessing) {
        if (namaPembeli.isBlank()) {
            statusMessage = "Nama Masih Kosong"
            isProcessing = false
        } else if (isProcessing) {
            statusMessage = "Memproses pesanan........."
            delay(5.seconds) // 5 detik
            statusMessage = "Tiket telah dipesan"
            isProcessing = false
        } else {
              if (statusMessage == "Nama Masih Kosong") {
                statusMessage = "Siap dipesan"
            }
        }
    }

    val totalHarga = hargaTiket * jumlahTiket

    PemesananTiketScreen(
        hargaTiket = hargaTiket,
        jumlahTiket = jumlahTiket,
        namaPembeli = namaPembeli,
        totalHarga = totalHarga,
        statusMessage = statusMessage,
        isProcessing = isProcessing,
        onJumlahChange = { newJumlah ->
            if (newJumlah >= 1) jumlahTiket = newJumlah
        },
        onNamaChange = { newNama ->
            namaPembeli = newNama
        },
        onPesanClick = {
            if (namaPembeli.isNotBlank() && !isProcessing) {
                isProcessing = true
            }
        },
        onResetClick = {
            jumlahTiket = 1
            namaPembeli = ""
            isProcessing = false
            statusMessage = "Nama Masih Kosong"
        }
    )
}

@Composable
fun PemesananTiketScreen(
    hargaTiket: Int,
    jumlahTiket: Int,
    namaPembeli: String,
    totalHarga: Int,
    statusMessage: String,
    isProcessing: Boolean,
    onJumlahChange: (Int) -> Unit,
    onNamaChange: (String) -> Unit,
    onPesanClick: () -> Unit,
    onResetClick: () -> Unit
) {
    fun formatRupiah(number: Int): String {
        val localeID = Locale.forLanguageTag("id-ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        format.maximumFractionDigits = 0
        return format.format(number)
    }

    val primaryBlue = Color(0xFF1E88E5)
    val cardBackground = Color.White
    val backgroundLight = Color(0xFFF5F7FA)
    val greenTotal = Color(0xFF1B5E20)
    val redReset = Color(0xFFE53935)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundLight
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = primaryBlue,
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                        .padding(top = 48.dp, bottom = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = "Tiket Icon",
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Halaman Tiket Konser",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Silakan isi data pemesanan di bawah",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Nama Pembeli Tiket",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = namaPembeli,
                                onValueChange = onNamaChange,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Masukkan nama lengkap") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Person Icon",
                                        tint = primaryBlue
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                enabled = !isProcessing
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Harga Tiket",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = formatRupiah(hargaTiket),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                            Text(
                                text = "per tiket",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }


                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Jumlah Tiket",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { onJumlahChange(jumlahTiket - 1) },
                                    enabled = (!isProcessing) && (jumlahTiket > 1),
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            if (!isProcessing && jumlahTiket > 1) primaryBlue else Color.LightGray,
                                            CircleShape
                                        )
                                ) {
                                    Text(
                                        text = "—",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(44.dp)
                                        .background(Color(0xFFF0F4F8), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = jumlahTiket.toString(),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }

                                IconButton(
                                    onClick = { onJumlahChange(jumlahTiket + 1) },
                                    enabled = !isProcessing,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(if (!isProcessing) primaryBlue else Color.LightGray, CircleShape)
                                ) {
                                    Text(
                                        text = "+",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }


                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackground),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Pembayaran",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = formatRupiah(totalHarga),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = greenTotal
                                    )
                                }


                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = when {
                                                statusMessage.contains("Memproses") -> Color(0xFFFFF3E0)
                                                statusMessage.contains("dipesan") && !statusMessage.contains("Kosong") -> Color(0xFFE8F5E9)
                                                else -> Color(0xFFFFEBEE)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Status : $statusMessage",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            statusMessage.contains("Memproses") -> Color(0xFFEF6C00)
                                            statusMessage.contains("dipesan") && !statusMessage.contains("Kosong") -> Color(0xFF2E7D32)
                                            else -> Color(0xFFC62828)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPesanClick,
                    enabled = !isProcessing && namaPembeli.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Order",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pesan Tiket",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = onResetClick,
                    enabled = !isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = redReset)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RESET",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PemesananTiketPreview() {
    PemesananTiketRoot()
}
