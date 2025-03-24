package eu.iamkonstantin.gadulkaplayer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform