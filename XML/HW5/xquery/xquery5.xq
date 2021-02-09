(: Returns an xhtml-document with all the breweries and their beer. :)

element html {
	element head {
    	element title {"Breweries"}
    },
    element body {
		element h1 {"Breweries"},
		for $brewery in fn:doc('data.xml')//brewery
		return (
			element h2 {
 	 	  	text {$brewery/name}
	 	   },
           element ul {
           	element li {"Beers"},
           	element ul {
            	for $beer in $brewery//beer
                return
                    	element li {$beer/name}
            }
           })
    }
}
