(: Shows every type of beer listed, all the beers belonging in that type and
   the number of beers in the group. :)

for $beertype in fn:doc('data.xml')//beer_type
return 
	element beerTable {
    	element beerType {
        text {$beertype/name}
    	},
     	element beerCount {
     	count(//beer/type[@typeRef = $beertype/@beerID])
   		},
        element beers {
        for $beer in //beer/type[@typeRef = $beertype/@beerID]
        return
        	element beer {
            	text {$beer/../name}
            }
        }
}