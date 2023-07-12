package com.example.gymapp.ui.screen.enumeration

sealed class ErrorCode: RuntimeException() {
    object None: ErrorCode()
    class ThirdPartyServiceError(override val message: String, thirdparyName: String): ErrorCode()
    class ThirdPartyServiceException(override val message: String, cause: Exception): ErrorCode()
    class InternalClientError(override val message: String): ErrorCode()
    class InternalClientException(override val message: String, cause: Exception): ErrorCode()
    class InternalServiceError(override val message: String): ErrorCode()
    class InternalServiceException(override val message: String, cause: Exception): ErrorCode()
}