# SYMlab2

## 4.1 Traitement des erreurs
*<u>Les classes et interfaces SymComManager et CommunicationEventListener utilisées au point 3.1
restent très (et certainement trop) simples pour être utilisables dans une vraie application : que se
passe-t-il si le serveur n’est pas joignable dans l’immédiat ou s’il retourne un code HTTP d’erreur ?
Veuillez proposer une nouvelle version, mieux adaptée, de ces deux classes / interfaces pour vous aider
à illustrer votre réponse.</u>*

Si un code d'erreur est généré dans l'état actuel de l'application, l'application continuera sont exécution sans qu'aucune erreur n'ait été générée. Concrètement si une erreur est générée, elle sera affichée comme si le message d'erreur est la réponse que l'on attendait de la part du serveur. Ce message d'erreur sera donc affiché dans la réponse et la requête sera considérée comme traitée.

Le minimum à faire pour géré au moins le cas ou le serveur n'est pas joignable pour une quelconque raison est de vérifier si une erreur est survenue. Le but est donc de tester dans le code si la réponse retournée contient bien le code 200, qui correspond à HTTP_OK (réponse du serveur qui annonce que tout s'est bien passé).
Il existe bien d'autres erreurs (401 : utilisateur non authentifil, 403 : accès refusé, 404 : page non trouvée) qu'il serait intéressant de vérifier pour donner les informations les plus précises à l'utilisateur et que celui-ci comprenne ce qui n'a pas fonctionné et ce qu'il peut éventuellement faite pour remédier au problème.

## 4.2 Authentification
*<u>Si une authentification par le serveur est requise, peut-on utiliser un protocole asynchrone ? Quelles
seraient les restrictions ? Peut-on utiliser une transmission différée ?</u>*

Une authentification par le serveur ne pose aucun problème dans le cas d'une communication asynchrone. En effet le serveur peut très bien exiger du client une authentification. Le serveur va donc demander au client d'envoyer dans chaque requête un token d'identification pour traiter la requête si la session du client est toujours valide. Les échanges d'informations sont indispensables dans l'autre sens, ainsi, si le serveur valide l'authentification du client, il lui transmettra via la réponse les informations relatives à l'authentification.

Cela implique de devoir ajouter un champs en plus pour renseigner l'authentification. Il faut donc un traitement en plus de la part du serveur et il faut que le client traite aussi l'authentification correctement. En général cela passe par la génération d'un ID unique de la part du serveur mais il faut malgré tout des étapes supplémentaires de traitement.

Enfin pour le cas d'une transmission différée, il est compliqué de gérer le cas d'une authentification. Cela requiert une communication directe avec le serveur au moment ou la demande est envoyée. La transmission différée implique que la connexion avec le serveur n'est pas toujours disponible, ainsi les échanges relatifs à l'authentification ne pourront peut-être pas être effectués.

## 4.3 Threads concurrents
*<u>Lors de l'utilisation de protocoles asynchrones, c'est généralement deux threads différents qui se
préoccupent de la préparation, de l'envoi, de la réception et du traitement des données. Quels
problèmes cela peut-il poser ?</u>*

