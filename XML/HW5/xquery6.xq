(: Returns only my favorite beers, marked by the attribute "is_delicious" :)

for $beer in //beer
return ( 
	if($beer/@is_delicious) then (    	
		fn:concat(fn:distinct-values($beer/name), " is delicious." )		
    )
    else ()
)