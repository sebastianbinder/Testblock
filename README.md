# Installation

Just add the `.jar`-file to your servers `plugins` directory. There is no need for editing any config or setting up any permissions.
If you'd like to have a default testblock, please add it as a `testblock.schematic` to a subdirectory called `testblocks` in your WorldEdit schematic directory (e.g. `plugins/WorldEdit/schematics/testblocks/testblock.schematic`).

# Usage

## Option 1: Store custom testblock
Make a WorldEdit region selection and use `//copy` to copy the testblock to your clipboard. Afterwards use `/testblock saveblock <name>` to save your custom testblock. You can create many testblocks this way. Selecting is as simple as using `/testblock select <name>`.

## Option 2: Use default testblock
No need to do anything if you would like to use the default testblock added by the server owner.

## Setting the location
Any player will be able to set a testblock-location with `/testblock setlocation`. The exact location as well as the direction the player is looking is important.

## Pasting the testblock
After having set the location, the player can move away and use `/testblock paste` to paste the block. The location set before will be the location the testblock has been copied before. The testblock will automatically be rotated if the direction the player has been looking on `/testblock setlocation` differs from the direction of the schematic.
