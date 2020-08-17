# FlickrMigration
## Run program from terminal: 

Setup:
```
git clone git@github.com:sophiabuell/FlickrMigration.git
cd FlickrMigration
```
Run:
```
./runProgram.sh
```
This will run the precompiled version. 
## Actions:
### Generate CSV of Collection information
This action downloads the information for all photos contained in the collection. 
It then prints it to a csv with the format of: 

```id, created, title, width, height, url```

This csv is save in metadata file with the name:
 <collection_id>_metadata.csv

If multiple sizes for a photo are available the largest option is selected.
### Download all photos in an album: 
This action downloads all photos in the given album. 
Each photo is saved to a folder media under the name: 
<photo_id>.<photo_ext>

If multiple sizes of a photo are available the largest option is selected. 