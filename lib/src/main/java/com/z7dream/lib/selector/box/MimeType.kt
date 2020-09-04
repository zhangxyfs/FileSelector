package com.z7dream.lib.selector.box

enum class MimeType {
    //pic
    JPEG {
        override fun getValue() = MimeTypeManager.arraySetOf("jpg", "jpeg")
        override fun getKey() = "image/jpeg"
    },
    JPE {
        override fun getValue() = MimeTypeManager.arraySetOf("jpe")
        override fun getKey() = "image/jpe"
    },
    GIF{
        override fun getValue() = MimeTypeManager.arraySetOf("gif")
        override fun getKey() = "image/gif"
    },
    BMP {
        override fun getValue() = MimeTypeManager.arraySetOf("bmp")
        override fun getKey() = "image/x-ms-bmp"
    },
    PNG {
        override fun getValue() = MimeTypeManager.arraySetOf("png")
        override fun getKey() = "image/png"
    },
    WEBP{
        override fun getValue() = MimeTypeManager.arraySetOf("webp")
        override fun getKey() = "image/webp"
    },
    //audio
    AUDIO{
        override fun getValue() = MimeTypeManager.arraySetOf("audio*")
        override fun getKey() = "audio/"
    },
    AAC {
        override fun getValue() = MimeTypeManager.arraySetOf("aac")
        override fun getKey() = "audio/aac"
    },
    MP3 {
        override fun getValue() = MimeTypeManager.arraySetOf("mp3")
        override fun getKey() = "audio/mpeg"
    },
    MID {
        override fun getValue() = MimeTypeManager.arraySetOf("mid")
        override fun getKey() = "audio/midi"
    },
    MID1 {
        override fun getValue() = MimeTypeManager.arraySetOf("mid")
        override fun getKey() = "audio/x-midi"
    },
    WAV {
        override fun getValue() = MimeTypeManager.arraySetOf("wav")
        override fun getKey() = "audio/wav"
    },
    FLAC {
        override fun getValue() = MimeTypeManager.arraySetOf("flac")
        override fun getKey() = "audio/flac"
    },
    AMR {
        override fun getValue() = MimeTypeManager.arraySetOf("amr")
        override fun getKey() = "audio/amr"
    },
    M4A {
        override fun getValue() = MimeTypeManager.arraySetOf("m4a")
        override fun getKey() = "audio/mpeg"
    },
    XMF {
        override fun getValue() = MimeTypeManager.arraySetOf("xmf")
        override fun getKey() = "audio/midi"
    },
    OGG {
        override fun getValue() = MimeTypeManager.arraySetOf("ogg")
        override fun getKey() = "audio/ogg"
    },
    APE {
        override fun getValue() = MimeTypeManager.arraySetOf("ape")
        override fun getKey() = "audio/ape"
    },
    //video
    _3GP {
        override fun getValue() = MimeTypeManager.arraySetOf("3gp")
        override fun getKey() = "video/3gpp"
    },
    MP4 {
        override fun getValue() = MimeTypeManager.arraySetOf("mp4")
        override fun getKey() = "video/mp4"
    },
    MKV {
        override fun getValue() = MimeTypeManager.arraySetOf("mkv")
        override fun getKey() = "video/mkv"
    },
    TS {
        override fun getValue() = MimeTypeManager.arraySetOf("ts")
        override fun getKey() = "video/mp2t"
    },
    RMVB {
        override fun getValue() = MimeTypeManager.arraySetOf("rmvb")
        override fun getKey() = "video/vnd.rn-realvideo"
    },
    AVI {
        override fun getValue() = MimeTypeManager.arraySetOf("avi")
        override fun getKey() = "video/x-msvideo"
    },
    MPEG1 {
        override fun getValue() = MimeTypeManager.arraySetOf("mpeg")
        override fun getKey() = "video/mpeg"
    },
    OGG1 {
        override fun getValue() = MimeTypeManager.arraySetOf("ogg")
        override fun getKey() = "video/ogg"
    },
    //txt
    TXT {
        override fun getValue() = MimeTypeManager.arraySetOf("txt")
        override fun getKey() = "text/plain"
    },
    JSON{
        override fun getValue() = MimeTypeManager.arraySetOf("json")
        override fun getKey() = "application/json"
    },
    //excel
    XLX {
        override fun getValue() = MimeTypeManager.arraySetOf("xla", "xlc", "xlm", "xls", "xlt", "xlw")
        override fun getKey() = "application/vnd.ms-excel"
    },
    XLSX {
        override fun getValue() = MimeTypeManager.arraySetOf("xlsx")
        override fun getKey() = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    },
    XLTX {
        override fun getValue() = MimeTypeManager.arraySetOf("xltx")
        override fun getKey() = "application/vnd.openxmlformats-officedocument.spreadsheetml.template"
    },
    //ppt
    DPS {
        override fun getValue() = MimeTypeManager.arraySetOf("dps")
        override fun getKey() = "application/ksdps"
    },
    DPT {
        override fun getValue() = MimeTypeManager.arraySetOf("dpt")
        override fun getKey() = "application/ksdpt"
    },
    PPT {
        override fun getValue() = MimeTypeManager.arraySetOf("ppt", "pot", "pps")
        override fun getKey() = "application/vnd.ms-powerpoint"
    },
    PPTX {
        override fun getValue() = MimeTypeManager.arraySetOf("pptx")
        override fun getKey() = "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    },
    //word
    WPS {
        override fun getValue() = MimeTypeManager.arraySetOf("wps", "wpt")
        override fun getKey() = "application/vnd.ms-works"
    },
    DOC {
        override fun getValue() = MimeTypeManager.arraySetOf("doc", "dot")
        override fun getKey() = "application/msword"
    },
    RTF {
        override fun getValue() = MimeTypeManager.arraySetOf("rtf")
        override fun getKey() = "application/rtf"
    },
    DOCX {
        override fun getValue() = MimeTypeManager.arraySetOf("docx")
        override fun getKey() = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    },
    //pdf
    PDF {
        override fun getValue() = MimeTypeManager.arraySetOf("pdf")
        override fun getKey() = "application/pdf"
    },
    //zip
    RAR{
        override fun getValue() = MimeTypeManager.arraySetOf("rar")
        override fun getKey() = "application/x-rar-compressed"
    },
    ZIP{
        override fun getValue() = MimeTypeManager.arraySetOf("zip")
        override fun getKey() = "application/zip"
    },
    _7Z{
        override fun getValue() = MimeTypeManager.arraySetOf("7z")
        override fun getKey() = "application/x-7z-compressed"
    },
    Z{
        override fun getValue() = MimeTypeManager.arraySetOf("z")
        override fun getKey() = "application/x-compress"
    },
    ISO{
        override fun getValue() = MimeTypeManager.arraySetOf("iso")
        override fun getKey() = "application/x-iso9660-image"
    },
    GZ{
        override fun getValue() = MimeTypeManager.arraySetOf("gz")
        override fun getKey() = "application/x-gzip"
    },
    TAR{
        override fun getValue() = MimeTypeManager.arraySetOf("tar")
        override fun getKey() = "application/x-tar"
    },
    APK{
        override fun getValue() = MimeTypeManager.arraySetOf("apk")
        override fun getKey() = "application/vnd.android.package-archive"
    },
    FOLDER{
        override fun getValue() = MimeTypeManager.arraySetOf("")
        override fun getKey() = ""
    };


    abstract fun getValue(): Set<String>
    abstract fun getKey(): String
}