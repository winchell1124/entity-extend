all: copy

clean:
	mvn clean
	rm -rf entity-extend entity-extend.tgz

package: clean
	mvn package

copy:
    rm -rf entity-extend entity-extend.tgz
	mkdir entity-extend
	cp `find target -maxdepth 1 -name "entity-extend*.jar"` entity-extend/
	cp -r target/lib entity-extend/
	cp `find bin -name "*.sh"` entity-extend/
	cp -r config entity-extend/
	tar zcvf entity-extend.tgz entity-extend/
