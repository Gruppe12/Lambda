

// Stores new user_id in local storage.
function storeNewLogin(user_id){

    localStorage.setItem('user_id', user_id)    
}


// What happens when you press the "logg på" button
function loginAsUser(){

    // Gets all info about the dropdown menu
    const dropdown = document.getElementById("users");

    // Gets the current value from the dropdown menu
    const selectedValue = dropdown.value;
    
    // Saves the user-id locally
    storeNewLogin(selectedValue)

    // Goes to the next page.
    window.location.href = "input.html";
}

