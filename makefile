run: starwarsapi up

starwarsapi:
	mvn compile jib:dockerBuild
up:
	docker-compose up -d
