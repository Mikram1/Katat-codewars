(: Returns all beers by a requested brewery. This one couldn't be tested with the 
   xquery-tool, so I hope it works. :)

declare function local:listBeersByBrewery($reqBrewery) as element()*
{
	for $brewery in fn:doc('data.xml')//brewery
    where some $bName in $brewery/name 
    satisfies $reqBrewery
    
    return $brewery//beer
};

<exampleCall>{local:listBeersByBrewery("Pilsner Urquell Brewery")}</exampleCall>