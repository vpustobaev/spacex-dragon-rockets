# SpaceX Dragon Repository
A simple, in-memory Java library to manage SpaceX missions and rocket assignments.

## Design Assumptions
* I assumed that when a Mission is marked as "Ended", its rockets are released (unassigned). This decision was based on the provided example data showing "Ended" missions with 0 assigned rockets.
* If the last rocket is removed from a mission (or the mission ends), the mission status reverts to "Scheduled".
* The summary sorts missions first by rocket count (descending), then by mission name (descending Z-A), adhering to the specific requirement.

## How to Run
This is a standard Java library. You can run the tests using JUnit 5.

## Work summary
* **Methodology:** I followed TDD
* **Role of AI:** I used an LLM (Google Gemini) as a pair programming partner during development. The AI helped verify edge cases, suggested optimizations for list iteration, and acted as a code reviewer.
* **Implementation:** All implementation logic and architectural decisions were made by me.
* **Problems:** The input data example shown states that the Rocket 'Red Dragon' should have status 'On ground' AND at the same time be assigned to the Mission 'Transit'.  
This contradicts to the described condition for the 'On ground' status – '...initial status, where the rocket is not assigned to any mission'. I took the liberty to adhere to the latter.