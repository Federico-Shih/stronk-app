package com.example.stronk.misc

import android.R.attr.bitmap
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


class QrCodeGenerator {
    companion object {
        fun qrUrlOfRoutine(routineId: Int): String {
            return "https://chart.googleapis.com/chart?chs=500x500&cht=qr&chl=https://www.stronk.com/routines/$routineId"
        }
    }
}