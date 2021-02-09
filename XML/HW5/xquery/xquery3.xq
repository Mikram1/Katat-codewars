(: Lists data from every brewery containing exactly one beer. :)

for $brewery in fn:doc('data.xml')//brewery
let $beer := $brewery//beer
let $beercount := fn:count($beer)
let $sum := sum($beercount)					(: I know this does very little, but I didn't come up with any other use for avg/min/max/sum. :)
where sum($beercount) = 1
return 
    <brewery>
    {$brewery/name},
    <beer>{data($brewery/beers/beer/name)}</beer>,
    </brewery>







