

// Check for valid user id.
// Sends you back to login page if not found.
// Replace is used to the user cant press the back button in the browser
function checkForLogin(){

    let user_id = localStorage.getItem('user_id')   
    let user_name = localStorage.getItem('user_name') 

    if (user_id == null || user_name == null){
        window.location.replace("login.html");
    } else{

        console.log("User id: ", user_id, "(", user_name, ")")

    }
}

// Get the locally stored User Id and sends it back as an int.
function getUserId() {
    let user_id = localStorage.getItem('user_id');  
    user_id = parseInt(user_id, 10); 

    return user_id;
}

// Get the locally stored User Id and sends it back as an int.
function getUserName() {
    const user_name = localStorage.getItem('user_name');  
    return user_name;
}



// Clears local storage, removing stored user-id
function logOut(){
   // Clears local storage
   localStorage.clear()

   // Checks if we have a stored user-id and if not sends us back to login page
   checkForLogin();
}


