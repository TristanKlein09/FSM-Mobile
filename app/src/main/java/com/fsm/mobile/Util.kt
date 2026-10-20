package com.fsm.mobile

import android.content.Context
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

//Using context allows the program to access the assets directory
class Util(private val context: Context) {
    private val propertiesFile = "mobile.properties"

    /**
     * Since the assets folder is read-only, to be able to make changes to the `mobile.properties`
     * file, we use a version from the device's storage. If it already exists, just use that one
     * otherwise, copy the file from the assets folder to the storage.
     * @return `Properties` of the `mobile.properties`
     */
    fun loadPropertiesFile() : Properties {
        val properties = Properties()
        val file = context.getFileStreamPath(propertiesFile)

        if (file.exists()) {
            context.openFileInput(propertiesFile).use {
                properties.load(it)
            }
        } else {
            context.assets.open(propertiesFile).use {
                properties.load(it)
            }
        }

        return properties
    }

    /**
     * Retrieves the value of a property from the `mobile.properties` file.
     * @param key The key for which the value should be retrieved.
     * @return A `String` which is the value of the key.
     */
    fun retrieveProperty(key: String): String{
        val properties = loadPropertiesFile()

        context.assets.open("mobile.properties").use {
            properties.load(it) //it is the default name for a parameter being passed into a lambda
        }

        val value = properties.getProperty(key)
        return value
    }

    /**
     * Changes the value of a property in the `mobile.properties` file.
     * @param key The property to change.
     * @param value The value to change the property to.
     */
    fun changeProperty(key: String?, value: String?) {
        val properties = loadPropertiesFile()

        properties.setProperty(key, value)

        //Context.MODE_PRIVATE specifies the permissions of the file created
        //Its created inside the app's storage and only the app can read/write to it
        context.openFileOutput(propertiesFile, Context.MODE_PRIVATE).use {
            properties.store(it, null)
        }
    }
}

