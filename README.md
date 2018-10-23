# Alchemy

This is an Android app for displaying the daily schedule and workouts at [Alchemy](https://alchemy365.com/)

## Purpose

Alchemy is a gym that combines yoga, strength, and conditioning training in a variety of class formats. Their website displays the daily schedule and workouts, but it's slow and cumbersome. I decided to make an app to quickly get the information I want daily.

## UX
When the user opens the app, the schedule for the current day will load and display. The user can then use the modals that appear by pressing buttons in the navigation bar to change date or to filter the displayed events by location.

The user can tab over to the workout page to view the workouts of the current day. As with the schedule page, the user can press a button in the navigation bar to select a different date. 

## Challenges

Alchemy does not have a public API. Therefore, I used browser dev tools to inspect the network requests that were being made on the relevant pages, and then I mimicked those requests. 

The schedule network request goes to an internal API that returns data in a standard JSON format. Locations come back as IDs without any name associated with them, and I'm not aware of an API endpoint that I can use to retrieve that information, so I hardcoded a mapping between the IDs and  location names. There are only a few locations, so this shouldn't cause too large of scalability issues.

Retrieving workout infromation was more difficult. The main web content is served statically, so I had to scrape HTML. The HTML documents aren't cleanly organized; I'm guessing they were created in a visual editor of some kind. Below is an example of a subsection of HTML that the app would need to parse:

```html
<div class="mk-single-content clearfix" itemprop="mainEntityOfPage">
	<p><strong>A10</strong><br>
<span data-sheets-value="{&quot;1&quot;:2,&quot;2&quot;:&quot;15-10-5: Torpedo Clusters, Torpedo Abmat Sit-Ups *EMOM: 5 Ring Dips&quot;}" data-sheets-userformat="{&quot;2&quot;:4483,&quot;3&quot;:[null,0],&quot;4&quot;:[null,2,16711680],&quot;10&quot;:2,&quot;11&quot;:4,&quot;15&quot;:&quot;arial,sans,sans-serif&quot;}">15-10-5 reps:<br>
Torpedo Clusters<br>
Torpedo Abmat Sit-Ups</span></p>
<p>Every Minute On the Minute: 5 Ring Dips</p>
<p>&nbsp;</p>
<hr>
<p>&nbsp;</p>
<p><strong>A20<br>
</strong><span data-sheets-value="{&quot;1&quot;:2,&quot;2&quot;:&quot;15-10-5: Torpedo Clusters, Torpedo Abmat Sit-Ups *EMOM: 5 Ring Dips&quot;}" data-sheets-userformat="{&quot;2&quot;:4483,&quot;3&quot;:[null,0],&quot;4&quot;:[null,2,16711680],&quot;10&quot;:2,&quot;11&quot;:4,&quot;15&quot;:&quot;arial,sans,sans-serif&quot;}">20-15-10-5 reps:<br>
Torpedo Clusters<br>
Torpedo Abmat Sit-Ups</span></p>
<p>Every Minute On the Minute: 5 Ring Dips</p>
<p>&nbsp;</p>
<hr>
<p>&nbsp;</p>
<p><strong>AStrong</strong><br>
<span data-sheets-value="{&quot;1&quot;:2,&quot;2&quot;:&quot;Primary Pull - Sumo Deadlift 3-3-3-3-3 -- Superset: Turbo Burpees 5x8&quot;}" data-sheets-userformat="{&quot;2&quot;:4483,&quot;3&quot;:[null,0],&quot;4&quot;:[null,2,16776960],&quot;10&quot;:2,&quot;11&quot;:4,&quot;15&quot;:&quot;arial,sans,sans-serif&quot;}">Sumo Deadlift 3-3-3-3-3<br>
Turbo Burpees 5×8</span></p>
<p><span data-sheets-value="{&quot;1&quot;:2,&quot;2&quot;:&quot;10-8-7 ... 3-2-1 Reps: \&quot;Leave it there\&quot; sit-ups, Torpedo seated floor press, strict pull-ups&quot;}" data-sheets-userformat="{&quot;2&quot;:4995,&quot;3&quot;:[null,0],&quot;4&quot;:[null,2,16711680],&quot;10&quot;:2,&quot;11&quot;:4,&quot;12&quot;:0,&quot;15&quot;:&quot;arial,sans,sans-serif&quot;}">&nbsp;</span></p>
<hr>
<p>&nbsp;</p>
<p><strong>APulse<br>
</strong><span data-sheets-value="{&quot;1&quot;:2,&quot;2&quot;:&quot;Clusters&quot;}" data-sheets-userformat="{&quot;2&quot;:4483,&quot;3&quot;:[null,0],&quot;4&quot;:[null,2,16711935],&quot;10&quot;:2,&quot;11&quot;:4,&quot;15&quot;:&quot;arial,sans,sans-serif&quot;}">Clusters</span></p>
</div>
```

My strategy for parsing workout information was to search the above outer div by class name, recursively pull out all text from the children elements, trim the text, filter out empty strings, and then add the rest to an array. Then, once I had an array of the all of the strings, I would check for the predesignated workout class names (A10, A20, AStrong, APulse). Once I found one of these, I would know that every upcoming string that wasn't one of the class names would belong to the most recently viewed class. With this information, I could construct native objects to represent the workouts.
