package ru.netology.nmedia.errorHandler

class ServiceErrorHandler() {
    companion object {
        fun getErrorDescription(code: String) = when (code) {
            "400" -> "Error 400: Your request could not be processed due to missing or invalid parameters."
            "401" -> "Error 401: You are not authorized to access this resource. Please log in or contact support for assistance."
            "403" -> "Error 403: Access to the requested resource is forbidden. Please check your permissions or contact support for assistance."
            "404" -> "Error 404: The requested resource could not be found. Please check the URL and try again or contact support for assistance."
            "500" -> "Error 500: There was an error on our server. Please try again later or contact support for assistance."
            "502" -> "Error 502: There was a problem with the server gateway. Please try again later or contact support for assistance."
            "503" -> "Error 503: The server is currently unavailable. Please try again later or contact support for assistance."
            else -> "Something went wrong. Try again later"
        }
    }
}
