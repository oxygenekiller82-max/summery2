//CHECK ACCESS TOKEN FIRST THING
if (!localStorage.getItem('access_token')) {
    window.location.href = '/login';
}

//intercepts the FORM of filtering in the catalog page 
document.querySelector('form').addEventListener('submit',async(event)=>{
	event.preventDefault(); // the flicker stops !! 
	
	//get data, form + THE URL !
	const formData = new FormData(event.target);
	const params = new URLSearchParams();
	if(formData.get('categoryName')) params.append('category', formData.get('categoryName'));
	if(formData.get('maxPrice')) params.append('maxPrice', formData.get('maxPrice'));
	
	if(formData.get('minNote')) params.append('minNote', formData.get('minNote'));
	
	try{
		const response = await authorizedFetch(`/api/products?${params.toString()}`);
		
		if (response.ok){
			const products = await response.json();
			//NOW JSON response not products..
			renderProducts(products.content || products);// THE GRID
		}
		
	}catch(error){
		console.error("Filtering failed:", error);
		//TODO CUSTOM EXCEPTION AS WELL ? possible ?
	}
});

//INITIAL , fetch products when page first opens 
document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const searchQuery = params.get('q');
    
	//see URL if it has search...
	 try {
	        let response;
	        if (searchQuery) {
	            // User came from home page search
	            document.getElementById('searchInput').value = searchQuery;
	            response = await authorizedFetch(`/api/products/search?q=${encodeURIComponent(searchQuery)}`);
	        } else {
	            // Normal load - all products
	            response = await authorizedFetch('/api/products');
	        }
	        
	        if (response.ok) {
	            const data = await response.json();
	            renderProducts(data.content || data);
	        }
	    } catch (err) {
	        console.error("Initial load failed", err);
	    }
	});


//SEARCH listner for products page:
document.getElementById('searchInput')?.addEventListener('input', async (e) => {
	
	const q = e.target.value.trim(); //CLEAN it , spaces at the end, start
	
	//no search bar = no saerch
	if (q.length===0){
		const res = await authorizedFetch('/api/products');
		if(res.ok){
		const data = await res.json();
		renderProducts(data.content || data);
		}
		 return;
		
	}
    if (q.length >= 2 ) {
		try{
			const params = new URLSearchParams();
			if (q) params.append('q', q); //transforms it to like: q=samsung
						
			
			const response = await authorizedFetch(`/api/products/search?${params.toString()}`);
		
        if (response.ok) {
            const data = await response.json();
            renderProducts(data.content || data);
			
        }
    }catch(err){
		console.error("Search failed:", err);
	}
	}
});


//render products function -> HTML injection into the grid rows 

function renderProducts(products){
	//get the grid container 
	const grid = document.getElementById('productGrid');
	if (!grid) return;
	grid.innerHTML = '';// clear all 
	
	//what if no products ? -> OH INJECT message h2 
	if (!products || products.length === 0) {
	        grid.innerHTML = '<div class="col-12 text-center py-5"><h3>No products found!</h3></div>';
	        return;
	    }
	
		
	//inner html injection 
	products.forEach(p=>{
		//iamges = array! 
		const mainImg = (p.images && p.images.length > 0) ? p.images : '/img/placeholder.png';
		
		grid.innerHTML+= `
			<div class="col-md-4">
				<div class="tactile-card p-3 h-100 text-center">
				
					<div style="height: 150px; background: #eee; border-radius: 12px; margin-bottom: 1rem; overflow: hidden;">
							<img src="${mainImg}" class="img-fluid" style="object-fit: contain; height: 100%; width: 100%;">
					</div>
				
					<h5 class="fw-bold">${p.nom}</h5>
					<p class="text-muted small">${p.description.substring(0, 50)}...</p>
					<p class="fw-bold" style="color: var(--accent-coral)">${p.prix} DT</p>
					<a href="/product/${p.id}" class="tactile-btn d-block py-2 text-decoration-none">Learn More</a>
				</div>
				
			</div>`;
	});	
}











