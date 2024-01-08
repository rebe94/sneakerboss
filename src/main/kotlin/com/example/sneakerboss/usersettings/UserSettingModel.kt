package com.example.sneakerboss.usersettings

import javax.validation.constraints.NotBlank

data class UserSettingModel(
    @NotBlank val sellerLevel: String,
    @NotBlank val region: String,
    @NotBlank val currencyCode: String
)