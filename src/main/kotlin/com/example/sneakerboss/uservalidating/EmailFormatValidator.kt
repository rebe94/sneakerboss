package com.example.sneakerboss.uservalidating

import com.example.sneakerboss.exceptions.InvalidEmailFormat
import org.springframework.stereotype.Component

@Component
class EmailFormatValidator {

    fun validEmailFormat(email: String) {
        var counter = 0
        email.toCharArray().forEach {
            if (it == '@') counter++
        }
        if (counter != 1) throw InvalidEmailFormat("Email address must contain only one '@' character")
    }
}