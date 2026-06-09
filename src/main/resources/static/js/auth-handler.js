// sheesh.. this has to INTERCEPT the submit in the login 
//then store the JWT token to local storage then add to to every header 
//in any request ohterwise session will be gone.. it's statless

async function handleLogin(event){
	//async.. SERVER RESPONSE! 
	event.preventDefault(); // no page refresh 
	
	const email=document.querySelector('input[name="username"]').value;
	const password=document.querySelector('input[name="password"]').value;
	
	const errorMessage = document.getElementById('errorMessage');
	errorMessage.style.display = 'none'; // hide
	
	if (password.length < 6) {
	        errorMessage.innerText = "Password must be at least 6 characters.";
	        errorMessage.style.display = 'block';
	        return;
	    }
		
	try{	
		const response=await fetch('/api/auth/authenticate',{
			method: 'POST',
			headers: {'Content-Type': 'application/json'},
			body: JSON.stringify({email,password}) // -> DTO fields match!! 
		});
	
		if (response.ok){
			const data=await response.json(); //await = pause THIS FUNCTION, but keep everything else
			localStorage.setItem('access_token',data.accessToken); //save the JWT token to the token key
			localStorage.setItem('refresh_token', data.refreshToken);
		
		
			window.location.href='/home' // WAIT WHAT PAGE IS NEXT ?? CATALOG FOR TESTING NOW
			
	}else {
		errorMessage.innerText = "Invalid Credentials, try logging in again";
		errorMessage.style.display = 'block';
	}
	
	}catch(err){
		errorMessage.innerText = "The server has encountered an error. Try again later.";
		errorMessage.style.display = 'block';
	}
	

}

document.getElementById('loginForm')?.addEventListener('submit', handleLogin);

//NOW WE HAVE THE TOKENS FINALLLLY 
//every action other than /api/auth must be authentticated 

async function fetchProtectedData(){
	const token = localStorage.getItem('access_token');
	
	const response=await fetch ('/api/products',{
		method: 'GET',
		headers:{
			//INJECTS AUTH HEADERS !! 
			'Authorization': `Bearer ${token}`,
			'Content-Type': 'application/json'
		}
	});
	
	if (repsonse.status==403){
		//now.. should refresh token here ???
		console.error("TOKEN EXPIRED OR INVALID");
	}
}

//-> every time ?? -> make it a function BRUH

async function authorizedFetch(url, options = {}) {
    
	const token = localStorage.getItem('access_token');
	
    const headers = {
        ...options.headers, // ... UNPACKS all the hearder huh,, then we ad dour own
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
	
	//add authorization bearer token 
	if(token && token!== null){
		headers['Authorization'] = `Bearer ${token}`;
	}
	
	
	try{
	let response=await fetch(url, { ...options, headers });
	
	//token expiration 
	if (response.status===403 || response.status===401){
		//refreshing access token
		const refreshToken = localStorage.getItem('refresh_token');
		
		if(!refreshToken){
			//no refresh token ??? -> brother login what is going on 
			window.location.href = '/login';
			return;
		}
		
		//else refresh it /api/auth/refresh-token
		const refreshRes = await fetch('/api/auth/refresh-token', {
		                method: 'POST',
		                headers: { 'Authorization': `Bearer ${refreshToken}` }
		            });
					
		if (refreshRes.ok){
			//get new tokenS: access_token
			const data = await refreshRes.json();
			localStorage.setItem('access_token', data.accessToken);
			
			//retry original request: 
			headers['Authorization'] = `Bearer ${data.accessToken}`;
			return await fetch(url, { ...options, headers });
			
		}else {
			//what if refresh URL fails.. session is done
			localStorage.clear();
			window.location.href = '/login';
		}
	}
	return response;
	}catch(err){
		throw err ;
	}
	
}
	

