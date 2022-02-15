package com.mahila.extended_floating_action_button
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.ConfigurationCompat

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mahila.extended_floating_action_button.databinding.ActivityMainBinding
import java.util.*

lateinit var sharedPre: SharedPreferences
var sharePreferencesValueOfLang: String? = null
const val SHARED_MODE_KEY = "MODE"
const val SHARED_LANG_KEY = "LANG"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPre = this.getSharedPreferences("sharedPre", Context.MODE_PRIVATE)
        sharedPre.getString(SHARED_MODE_KEY, "Auto")?.let { changeMode(it) }
        sharePreferencesValueOfLang = sharedPre.getString(SHARED_LANG_KEY, "Auto")
        sharePreferencesValueOfLang?.let {
            applyLocalized(it)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.settingsBtn.setOnClickListener {
            if (binding.settingsBtn.tag == "UNCLICK") {
                setUpLangButton()
                setUpModeButton(it)

                binding.settingsBtn.tag = "CLICK"
                binding.lanfBtn.visibility = View.VISIBLE
                binding.modeBtn.visibility = View.VISIBLE
            } else {
                binding.settingsBtn.tag = "UNCLICK"
                binding.lanfBtn.visibility = View.GONE
                binding.modeBtn.visibility = View.GONE
            }
        }
        binding.lanfBtn.setOnClickListener {
            changeLang()
            setUpLangButton()

        }
        binding.modeBtn.setOnClickListener {
            changeMode()
            setUpModeButton(it)
        }


    }

    private fun changeLang() {
        when (sharePreferencesValueOfLang) {
            "ar" -> {
                applyLocalized("en")
                sharedPre.edit().putString(SHARED_LANG_KEY, "en").apply()
                setUpLangButton()
            }
            "en" -> {
                applyLocalized("ar")
                sharedPre.edit().putString(SHARED_LANG_KEY, "ar")
                    .apply()
                setUpLangButton()
            }
            else -> {
                println("else")
                applyLocalized(reverseCurrentLang())
                sharedPre.edit().putString(SHARED_LANG_KEY, reverseCurrentLang())
                    .apply()
                setUpLangButton()

            }
        }
        this.recreate()
    }

    private fun setUpLangButton() {
        when (sharePreferencesValueOfLang) {

            "ar" -> {
                binding.lanfBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_en
                    )
                )

            }
            "en" -> {

                binding.lanfBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_ar
                    )
                )
            }
            else -> {
                if (reverseCurrentLang() == "en") binding.lanfBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_en
                    )
                ) else binding.lanfBtn.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_ar
                    )
                )
            }
        }
    }

    private fun reverseCurrentLang(): String {
        return if (ConfigurationCompat.getLocales(
                Resources.getSystem().configuration
            ).get(0).language == "en"
        ) {
            "ar"
        } else "en"
    }



    private fun changeMode(sharePreferencesValueOfMode:String) {
        when (sharePreferencesValueOfMode) {
            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
            }
        }
    }

    private fun changeMode(){

        when (sharedPre.getString(SHARED_MODE_KEY, "Auto")) {
            "LIGHT" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPre.edit().putString(SHARED_MODE_KEY, "DARK").apply()
            }
            "DARK" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPre.edit().putString(SHARED_MODE_KEY, "LIGHT")
                    .apply()
            }
            else -> {
                if (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                    == Configuration.UI_MODE_NIGHT_YES
                ) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPre.edit().putString(SHARED_MODE_KEY, "LIGHT")
                        .apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPre.edit().putString(SHARED_MODE_KEY, "DARK")
                        .apply()
                }
            }
        }
    }

    private fun setUpModeButton(view: View?) {
        when (sharedPre.getString(SHARED_MODE_KEY, "Auto")) {
            "LIGHT" -> {
                if (view != null) {
                    binding.modeBtn.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_moon
                        )
                    )
                }
            }
            "DARK" -> {
                if (view != null) {
                    binding.modeBtn.setImageDrawable(
                        AppCompatResources.getDrawable(
                            view.context,
                            R.drawable.ic_sun
                        )
                    )
                }
            }
            else -> {

                when (this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> {
                        if (view != null) {
                            binding.modeBtn.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    view.context,
                                    R.drawable.ic_moon
                                )
                            )
                        }
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {
                        if (view != null) {
                            binding.modeBtn.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    view.context,
                                    R.drawable.ic_sun
                                )
                            )
                        }
                    }
                }


            }
        }
    }

    private fun applyLocalized(_langCode: String) {

        if (_langCode != "Auto") {
            println("Auto")

            val locale = Locale(_langCode)
            Locale.setDefault(locale)
            val configuration = resources.configuration
            configuration?.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

    }




}