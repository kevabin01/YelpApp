package com.codequark.yelp.utils

import androidx.annotation.IntDef
import androidx.annotation.StringDef

class Constants {
    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

        const val PAGE_DETAIL = 0
        const val PAGE_MAP = 1
        const val PAGE_LOCATION = 2
        const val PAGE_REVIEWS = 3

        const val TOTAL_PAGES_DETAIL = 4
    }

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        JsonConstants.query,
        JsonConstants.alias,
        JsonConstants.latitude,
        JsonConstants.longitude
    )
    annotation class JsonConstants {
        companion object {
            const val query = "query"
            const val alias = "alias"
            const val latitude = "latitude"
            const val longitude = "longitude"
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        ItemDef.HEADER,
        ItemDef.DIVIDER,
        ItemDef.CONTENT
    )
    annotation class ItemDef {
        companion object {
            const val HEADER = 1
            const val DIVIDER = 2
            const val CONTENT = 3
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        LoginStateDef.STATE_DEFAULT,
        LoginStateDef.STATE_LOGIN_SUCCESS,
        LoginStateDef.STATE_LOGIN_ERROR,
        LoginStateDef.STATE_LOGIN_ERROR_NETWORK,
        LoginStateDef.STATE_LOGIN_ERROR_EMAIL,
        LoginStateDef.STATE_LOGIN_ERROR_PASSWORD,
        LoginStateDef.STATE_LOGIN_ERROR_EMAIL_EMPTY,
        LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_EMPTY,
        LoginStateDef.STATE_LOGIN_ERROR_PASSWORD_LENGTH,
        LoginStateDef.STATE_LOGIN_ERROR_EXISTS,
        LoginStateDef.STATE_LOGIN_ERROR_NOT_EXISTS,
        LoginStateDef.STATE_LOGIN_ERROR_MANY_REQUESTS
    )
    annotation class LoginStateDef {
        companion object {
            const val STATE_DEFAULT = 0
            const val STATE_LOGIN_SUCCESS = 1
            const val STATE_LOGIN_ERROR = 2
            const val STATE_LOGIN_ERROR_NETWORK = 3
            const val STATE_LOGIN_ERROR_EMAIL = 4
            const val STATE_LOGIN_ERROR_PASSWORD = 5
            const val STATE_LOGIN_ERROR_EMAIL_EMPTY = 6
            const val STATE_LOGIN_ERROR_PASSWORD_EMPTY = 7
            const val STATE_LOGIN_ERROR_PASSWORD_LENGTH = 8
            const val STATE_LOGIN_ERROR_EXISTS = 9
            const val STATE_LOGIN_ERROR_NOT_EXISTS = 10
            const val STATE_LOGIN_ERROR_MANY_REQUESTS = 11
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        MainStateDef.STATE_DEFAULT,
        MainStateDef.STATE_FINISH,
        MainStateDef.STATE_GPS_ON,
        MainStateDef.STATE_GPS_OFF
    )
    annotation class MainStateDef {
        companion object {
            const val STATE_DEFAULT = 0
            const val STATE_FINISH = 1
            const val STATE_GPS_ON = 2
            const val STATE_GPS_OFF = 3
        }
    }
}