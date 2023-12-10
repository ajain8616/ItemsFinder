package com.example.alzheicare

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DataSetDetailsActivity : AppCompatActivity() {
    private lateinit var itemName: TextView
    private lateinit var description: TextView
    private lateinit var itemType: TextView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var editActionButton: FloatingActionButton
    private lateinit var deleteActionButton: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var database: DatabaseReference
    private lateinit var deleteMessageAnimation: LottieAnimationView
    private lateinit var containerMessage: LottieAnimationView
    private lateinit var getContainerButton:Button
    private lateinit var setContainerView:LinearLayout
    private lateinit var getContainerView:LinearLayout
    private lateinit var setContainerButton: Button
    private lateinit var editItemName: EditText
    private lateinit var editDescription: EditText
    private lateinit var radioGroupItemType: RadioGroup
    private lateinit var radioItem: RadioButton
    private lateinit var radioContainer: RadioButton
    private lateinit var saveChangesButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var updateMessage: LottieAnimationView
    private lateinit var linearLayoutForm: LinearLayout
    private lateinit var updatedCardView:CardView
    private lateinit var updatedItemName:TextView
    private lateinit var updatedDescription:TextView
    private lateinit var updatedItemType:TextView
    private lateinit var ObjectSelection:TextView
    private lateinit var InSelection:TextView
    private lateinit var ContainerSelection:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_set_details)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        currentUser = auth.currentUser!!
        setEventHandlers()
        displayData()
    }

    private fun setEventHandlers() {
        findIdsOfElements()
        if (currentUser != null) {
            getUpdatedDetails()
        }

        linearLayoutForm.visibility = View.GONE
        floatingActionButton.setOnClickListener {
            if (editActionButton.visibility == View.GONE || deleteActionButton.visibility == View.GONE) {
                editActionButton.visibility = View.VISIBLE
                deleteActionButton.visibility = View.VISIBLE
            } else {
                editActionButton.visibility = View.GONE
                deleteActionButton.visibility = View.GONE
            }
        }

        editActionButton.setOnClickListener {
            linearLayoutForm.visibility = View.VISIBLE
            editActionButton.visibility = View.GONE
            deleteActionButton.visibility = View.GONE
            cardView.visibility = View.GONE
            floatingActionButton.visibility = View.GONE
            editActionButton.visibility = View.GONE
            setContainerView.visibility=View.GONE
            getContainerView.visibility=View.GONE
            updateMessage.visibility = View.GONE
            updatedCardView.visibility=View.GONE
            backButton.visibility = View.VISIBLE

        }

        backButton.setOnClickListener {
            linearLayoutForm.visibility = View.GONE
            editActionButton.visibility = View.VISIBLE
            deleteActionButton.visibility = View.VISIBLE
            cardView.visibility = View.VISIBLE
            floatingActionButton.visibility = View.VISIBLE
            editActionButton.visibility = View.VISIBLE
            setContainerView.visibility=View.VISIBLE
            getContainerView.visibility=View.VISIBLE
            updateMessage.visibility = View.VISIBLE

        }

        saveChangesButton.setOnClickListener {
            setUpdateDetails()
            updateMessage.visibility = View.VISIBLE
            editActionButton.visibility = View.GONE
            deleteActionButton.visibility = View.GONE
            cardView.visibility = View.GONE
            floatingActionButton.visibility = View.GONE
            editActionButton.visibility = View.GONE
            setContainerView.visibility=View.GONE
            getContainerView.visibility=View.GONE
            linearLayoutForm.visibility = View.GONE
            updatedCardView.visibility=View.GONE
            deleteMessageAnimation.visibility = View.GONE
            backButton.visibility = View.GONE

            Handler(Looper.getMainLooper()).postDelayed({
                updateMessage.visibility = View.VISIBLE
                val intent = Intent(this@DataSetDetailsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

        }

        deleteActionButton.setOnClickListener {
            val itemName = itemName.text
            deleteDataFromFirebase(itemName.toString())
            deleteMessageAnimation.visibility = View.VISIBLE
            editActionButton.visibility = View.GONE
            deleteActionButton.visibility = View.GONE
            updatedCardView.visibility=View.GONE
            cardView.visibility = View.GONE
            floatingActionButton.visibility = View.GONE
            editActionButton.visibility = View.GONE
            setContainerView.visibility = View.GONE
            getContainerView.visibility = View.GONE
            linearLayoutForm.visibility = View.GONE
            updateMessage.visibility = View.GONE
            backButton.visibility = View.GONE

            Handler(Looper.getMainLooper()).postDelayed({
                deleteMessageAnimation.visibility = View.VISIBLE
                val intent = Intent(this@DataSetDetailsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

        }

        setContainerButton.setOnClickListener {
            containerActivityAnimation()
            containerMessage.playAnimation()
        }

        getContainerButton.setOnClickListener {
            getContainerView.visibility=View.VISIBLE
            getContainerInItem()
        }

    }

    private fun findIdsOfElements() {
        itemName = findViewById(R.id.itemNameView)
        description = findViewById(R.id.descriptionView)
        itemType = findViewById(R.id.itemTypeView)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        editActionButton = findViewById(R.id.editActionButton)
        deleteActionButton = findViewById(R.id.deleteActionButton)
        cardView = findViewById(R.id.cardView)
        deleteMessageAnimation = findViewById(R.id.deleteMessage)
        containerMessage = findViewById(R.id.containerMessage)
        setContainerView = findViewById(R.id.setContainerView)
        getContainerView = findViewById(R.id.getContainerView)
        editItemName = findViewById(R.id.editItemName)
        editDescription = findViewById(R.id.editDescription)
        radioGroupItemType = findViewById(R.id.radioGroupItemType)
        radioItem = findViewById(R.id.radioItem)
        radioContainer = findViewById(R.id.radioContainer)
        saveChangesButton = findViewById(R.id.saveChangesButton)
        updateMessage = findViewById(R.id.updateMessage)
        linearLayoutForm = findViewById(R.id.linearLayoutForm)
        backButton = findViewById(R.id.backButton)
        updatedCardView = findViewById(R.id.updatedCardView)
        updatedItemName = findViewById(R.id.updatedItemName)
        updatedDescription = findViewById(R.id.updatedDescription)
        updatedItemType = findViewById(R.id.updatedItemType)
        setContainerButton=findViewById(R.id.setContainerButton)
        getContainerButton=findViewById(R.id.getContainerButton)
        ObjectSelection=findViewById(R.id.ObjectSelection)
        InSelection=findViewById(R.id.InSelection)
        ContainerSelection=findViewById(R.id.ContainerSelection)
    }

    private fun displayData() {
        val itemNameExtra = intent.getStringExtra("itemName")
        val descriptionExtra = intent.getStringExtra("description")
        val itemTypeExtra = intent.getStringExtra("itemType")
        itemName.text = itemNameExtra
        itemName.setTypeface(null, Typeface.BOLD)
        description.text = descriptionExtra
        itemType.text = itemTypeExtra

        when (itemTypeExtra) {
            "OBJECT" -> {
                itemType.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                cardView.setBackgroundResource(R.drawable.item_view_border_blue)
            }

            "CONTAINER" -> {
                itemType.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray))
                cardView.setBackgroundResource(R.drawable.item_view_border_gray)
            }
        }
    }

    private fun deleteDataFromFirebase(itemId: String) {
        val database = FirebaseDatabase.getInstance().reference
        val collectionName = "Item_Container_Data"
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = database.child(collectionName).child(userId).child(itemId)
            userRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    deleteMessageAnimation.playAnimation()
                } else {
                    Toast.makeText(this, "Failed to delete item $itemId", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_LONG).show()
        }
    }

    private fun containerActivityAnimation() {
        containerMessage.visibility = View.VISIBLE
        deleteMessageAnimation.visibility = View.GONE
        editActionButton.visibility = View.GONE
        deleteActionButton.visibility = View.GONE
        cardView.visibility = View.GONE
        setContainerView.visibility = View.GONE
        getContainerView.visibility = View.GONE
        floatingActionButton.visibility = View.GONE
        linearLayoutForm.visibility = View.GONE
        updateMessage.visibility = View.GONE
        updatedCardView.visibility=View.GONE
        backButton.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            containerMessage.visibility = View.VISIBLE
            val intent = Intent(this@DataSetDetailsActivity, ContainerChoiceActivity::class.java)
            intent.putExtra("itemName", itemName.text.toString())
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun setUpdateDetails() {
        val itemNameExtra = itemName.text.toString()
        val updatedDescription = editDescription.text.toString()

        if (updatedDescription.isNotEmpty()) {
            val database = FirebaseDatabase.getInstance().reference
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val selectedRadioButtonId = radioGroupItemType.checkedRadioButtonId

            // Check if a radio button is selected
            if (selectedRadioButtonId != -1 && userId != null) {
                val collectionName = "Updated_Objects_Containers"

                val userRef = database.child(collectionName).child(userId).child(itemNameExtra)

                // Get the selected item type from the radio button
                val updatedItemType = when (selectedRadioButtonId) {
                    R.id.radioItem -> "OBJECT"
                    R.id.radioContainer -> "CONTAINER"
                    else -> "" // Default item type if none selected
                }

                // Create a map with the updated data
                val updatedData = mapOf(
                    "description" to updatedDescription,
                    "itemType" to updatedItemType,
                )

                userRef.updateChildren(updatedData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Update UI or show a success message
                            updateMessage.playAnimation()
                            description.text = updatedDescription
                            itemType.text = updatedItemType
                        } else {
                            Toast.makeText(
                                this@DataSetDetailsActivity,
                                "Failed to update item $itemNameExtra",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@DataSetDetailsActivity,
                    "Please select an item type",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                this@DataSetDetailsActivity,
                "Please enter values for all fields",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getUpdatedDetails() {
        val userId = currentUser?.uid
        val database = FirebaseDatabase.getInstance().reference
        val collectionName = "Updated_Objects_Containers"
        val itemNameExtra = intent.getStringExtra("itemName")
        if (userId != null) {
            database.child(collectionName).child(userId).child(itemNameExtra!!).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        // Retrieve and display updated values
                        val updatedDescriptionValue = dataSnapshot.child("description").value.toString()
                        val updatedItemTypeValue = dataSnapshot.child("itemType").value.toString()
                        updatedItemName.text = itemNameExtra
                        updatedDescription.text = updatedDescriptionValue
                        updatedItemType.text = updatedItemTypeValue

                        when (updatedItemTypeValue) {
                            "OBJECT" -> {
                                updatedItemType.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
                                updatedCardView.setBackgroundResource(R.drawable.item_view_border_blue)
                            }

                            "CONTAINER" -> {
                                updatedItemType.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGray))
                                updatedCardView.setBackgroundResource(R.drawable.item_view_border_gray)
                            }
                        }



                        updatedCardView.visibility=View.VISIBLE
                    } else {
                        Toast.makeText(
                            this@DataSetDetailsActivity,
                            "No data available in the collection",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@DataSetDetailsActivity,
                        "Error getting updated values: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            Toast.makeText(
                this@DataSetDetailsActivity,
                "User not logged in. Updated values cannot be retrieved.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun getContainerInItem() {
        val userId = currentUser?.uid
        val itemNameExtra = intent.getStringExtra("itemName")
        val database = FirebaseDatabase.getInstance().reference
        val collectionName = "Item_In_Container"

        if (userId != null) {
            database.child(collectionName).child(userId).child(itemNameExtra!!).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        // Retrieve and display container details
                        val containerName = dataSnapshot.child("containerName").value.toString()
                        ObjectSelection.text = itemNameExtra
                        ContainerSelection.text = containerName
                    } else {
                        Toast.makeText(
                            this@DataSetDetailsActivity,
                            "No container details available for $itemNameExtra",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@DataSetDetailsActivity,
                        "Error getting container details: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            Toast.makeText(
                this@DataSetDetailsActivity,
                "User not logged in. Container details cannot be retrieved.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
