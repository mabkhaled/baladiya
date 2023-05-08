package com.example.baladeyti.components

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.baladeyti.R
import com.example.baladeyti.models.Extrait
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import java.io.*
import java.util.*

class Pdf(private val dataBill: Extrait, private val mContext: Context) {


    fun savePDF() {
        //var now         = Date()
        val filename = "Extrait.pdf"
        //val pathd        = File(mContext.getExternalFilesDir(null)!!.absolutePath + "/ShowappBills")
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toURI()
        )

        val folder = File(path, "baladeyti")
        if (!folder.exists()) folder.mkdirs()

        var file = File(folder, filename)

        if (file.exists()) {
            file.delete()
            file = File(folder, filename)
        }

        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(file))

        document.open()
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("")
        document.addCreator("")

        val mColorAccent = BaseColor(0, 153, 204, 255)
        val mHeadingFontSize = 20.0f
        val mValueFontSize = 26.0f


        val fontMontserrat =
            BaseFont.createFont("res/font/fontmontserrat.ttf", "UTF-8", BaseFont.EMBEDDED)
        val lineSeparator = LineSeparator()

        lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

        //adjust the size of the label
        val fontmontserratForHeader = Font(
            fontMontserrat,
            36.0f, Font.NORMAL,
            BaseColor.BLACK
        )

        val fontnormalcolorBlack = Font(
            fontMontserrat,
            mValueFontSize,
            Font.NORMAL,
            BaseColor.BLACK
        )
        val fontnormalcolorBlue = Font(
            fontMontserrat,
            mValueFontSize,
            Font.NORMAL,
            mColorAccent
        )
        val fontheading = Font(
            fontMontserrat,
            mHeadingFontSize,
            Font.NORMAL,
            BaseColor.BLACK
        )

        // for the image
        fun saveImageToInternalStorage(drawableId: Int): Uri {
            // Get the image from drawable resource as drawable object
            val drawable = ContextCompat.getDrawable(mContext, drawableId)

            // Get the bitmap from drawable object
            val bitmap = (drawable as BitmapDrawable).bitmap

            // Get the context wrapper instance
            val wrapper = ContextWrapper(mContext)

            // Initializing a new file
            // The bellow line return a directory in internal storage
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)


            // Create a file to save the image
            file = File(file, "${UUID.randomUUID()}.jpg")

            try {
                // Get the file output stream
                val stream: OutputStream = FileOutputStream(file)

                // Compress bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

                // Flush the stream
                stream.flush()

                // Close stream
                stream.close()
            } catch (e: IOException) { // Catch the exception
                e.printStackTrace()
            }

            // Return the saved image uri
            return Uri.parse(file.absolutePath)
        }

        val uri: Uri = saveImageToInternalStorage(R.drawable.logo_def)
        Log.i("Yesss", uri.toString())

        //trying a table
        val imgFile = File(uri.toString())
        Log.i("yesss", imgFile.exists().toString())
        var img: Image? = null
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            val stream = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            img = Image.getInstance(byteArray)
        }
        Log.i("aasbaaa2", img.toString())
        //val Image = Image(img)
        img!!.alignment = Element.ALIGN_CENTER
        document.add(img)


        //application Name
        //val paragraf1 = Paragraph(Chunk(dataBill.AppName,fontmontserratForHeader))
        //paragraf1.alignment = Element.ALIGN_LEFT
        //document.add(paragraf1)

        //seprator 1
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))


        val table = PdfPTable(3)
        table.paddingTop = 250f
        table.totalWidth = 100f
        table.widthPercentage = 100f
        val cell = PdfPCell(Phrase(" Organisation "))
        val cell2 = PdfPCell(Phrase(" Email Address "))
        val cell3 = PdfPCell(Phrase(" Phone Number "))
        val cell4 = PdfPCell(Phrase(" Baladeyti "))
        val cell5 = PdfPCell(Phrase(" gbhy1919@gmail.com "))
        val cell6 = PdfPCell(Phrase(" 56766103 "))
        val cell7 = PdfPCell(Phrase(dataBill.address))
        val cell8 = PdfPCell(Phrase(dataBill.phoneNumber))

//        val cell = PdfPCell(Phrase(dataBill.lastName))
//        val cell2 = PdfPCell(Phrase(dataBill.firstName))
//        val cell3 = PdfPCell(Phrase(dataBill.birthdate))
//        val cell4 = PdfPCell(Phrase(dataBill.gender))
//        val cell5 = PdfPCell(Phrase(dataBill.civilStatus))
//        val cell6 = PdfPCell(Phrase(dataBill.cin.toString()))
//        val cell7 = PdfPCell(Phrase(dataBill.address))
//        val cell8 = PdfPCell(Phrase(dataBill.phoneNumber))

//        val cell = PdfPCell(Phrase(" Organisation "))
//        val cell2 = PdfPCell(Phrase(" Email Address "))
//        val cell3 = PdfPCell(Phrase(" Phone Number "))
//        val cell4 = PdfPCell(Phrase(" Baladeyti "))
//        val cell5 = PdfPCell(Phrase(" gbhy1919@gmail.com "))
//        val cell6 = PdfPCell(Phrase(" 56766103 ")

//        cell.setFixedHeight(13);
        //        cell.setFixedHeight(13);
        //cell.border = Rectangle.NO_BORDER
        //cell.colspan = 1
        //cell.backgroundColor = BaseColor.CYAN
        table.addCell(cell)
        table.addCell(cell2)
        table.addCell(cell3)
        table.completeRow()


        table.addCell(cell4)
        table.addCell(cell5)
        table.addCell(cell6)
//        table.addCell(cell7)
//        table.addCell(cell8)
        table.completeRow()
        document.add(table)


        //TITLE EXTRAIT
        val paragraf0 = Paragraph(Chunk("EXTRACT FROM THE CIVIL STATUS REGISTERS", fontnormalcolorBlack))
        paragraf0.alignment = Element.ALIGN_MIDDLE
        paragraf0.add(Chunk(VerticalPositionMark()))
        document.add(paragraf0)

//        val paragraf = Paragraph(Chunk("CIVIL", fontnormalcolorBlack))
//        paragraf.alignment = Element.ALIGN_MIDDLE
//        paragraf.add(Chunk(VerticalPositionMark()))
//        document.add(paragraf)

        //seprator 2
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))


        val paragraf1 = Paragraph(Chunk("BIRTH CERTIFICATE ", fontnormalcolorBlack))
        paragraf1.alignment = Element.ALIGN_MIDDLE
        paragraf1.add(Chunk(VerticalPositionMark()))
        document.add(paragraf1)

        //seprator 2
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))


        //lastName
        val paragraf2 = Paragraph(Chunk("Last name : ", fontnormalcolorBlack))
        //paragraf2.alignment = Element.ALIGN_LEFT
        paragraf2.add(Chunk(VerticalPositionMark()))
        paragraf2.add(dataBill.lastName)
        document.add(paragraf2)

        //firstName
        val paragraf3 = Paragraph("First name :", fontnormalcolorBlack)
        paragraf3.add(Chunk(VerticalPositionMark()))
        paragraf3.add(dataBill.firstName)
        document.add(paragraf3)

        //birthdate
        val paragraf4 = Paragraph("Birthday :", fontnormalcolorBlack)
        paragraf4.add(Chunk(VerticalPositionMark()))
        paragraf4.add(dataBill.birthdate)
        document.add(paragraf4)

        //gender
        val paragraf5 = Paragraph("Gender :", fontnormalcolorBlack)
        paragraf5.add(Chunk(VerticalPositionMark()))
        paragraf5.add(dataBill.gender)
        document.add(paragraf5)

        //civilStatus
        val paragraf6 = Paragraph("Civil Status :", fontnormalcolorBlack)
        paragraf6.add(Chunk(VerticalPositionMark()))
        paragraf6.add(dataBill.civilStatus)
        document.add(paragraf6)

        //cin
        val paragraf7 = Paragraph("Cin :", fontnormalcolorBlack)
        paragraf7.add(Chunk(VerticalPositionMark()))
        paragraf7.add(dataBill.cin.toString())
        document.add(paragraf7)

        //address
        val paragraf8 = Paragraph("Address :", fontnormalcolorBlack)
        paragraf8.add(Chunk(VerticalPositionMark()))
        paragraf8.add(dataBill.address)
        document.add(paragraf8)

        //phoneNumber
        val paragraf9 = Paragraph("Phone number :", fontnormalcolorBlack)
        paragraf9.add(Chunk(VerticalPositionMark()))
        paragraf9.add(dataBill.phoneNumber)
        document.add(paragraf9)

        //seprator 2
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        //total
//        val  paragraf5          =   Paragraph("Total", fontnormalcolorBlack)
//        paragraf5 .add(Chunk(VerticalPositionMark()))
//        paragraf5 .add(dataBill.Total)
//        document.add(paragraf5)


//        val uri_logo: Uri = saveImageToInternalStorage(R.drawable.building)
//
//        //trying a table
//        val imgFile_logo = File(uri_logo.toString())
//
//        var img_logo: Image? = null
//        if (imgFile_logo.exists()) {
//            val myBitmap = BitmapFactory.decodeFile(imgFile_logo.absolutePath)
//            val stream = ByteArrayOutputStream()
//            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            val byteArray: ByteArray = stream.toByteArray()
//            img_logo = Image.getInstance(byteArray)
//        }
//        Log.i("aasbaaa2", img.toString())
//        //val Image = Image(img)
//        img_logo!!.alignment = Element.ALIGN_LEFT
//        img_logo!!.alignment = Element.ALIGN_BOTTOM
//        document.add(img_logo)


        val uri_cachet: Uri = saveImageToInternalStorage(R.drawable.cachet)

        //trying a table
        val imgFile_cachet = File(uri_cachet.toString())

        var img_cachet: Image? = null
        if (imgFile_cachet.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile_cachet.absolutePath)
            val stream = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            img_cachet = Image.getInstance(byteArray)
        }
        Log.i("yes2", img.toString())
        //val Image = Image(img)

        img_cachet!!.alignment = Element.ALIGN_RIGHT
        img_cachet!!.alignment = Element.ALIGN_BOTTOM
        document.add(img_cachet)


        document.close()

    }
}