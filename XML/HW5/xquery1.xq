(: The goal is to include the beer's type explanation from the beer_types-section according to
   the type of the individual beer. In short, includes all items from each beer and adds 
   the type's explanation. Sorts by type. :)
   
for $b in fn:doc('data.xml')//beer
let $btype := fn:doc('data.xml')//beer_type[@beerID = $b/type/@typeRef]/explanation
order by $b/type
return 
	<beer>
    	<name>{data($b/name)}</name>
        <type>{data($b/type)}</type>
        <explanation>{data($btype)}</explanation>
        <alcohol_content>{data($b/alcohol_content)}</alcohol_content>
    </beer>