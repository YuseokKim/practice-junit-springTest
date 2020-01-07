# practice-junit-springTest
junit 및 spring-test 인프런 강의 복습입니다

 
## 테스트 의존성 추가 
 - spring-boot-starter-test, spring-boot-starter-web, spring-boot-starter-webflux(WebTestClient 사용 시 필요) 의존성 추가
   
## @SpringBootTest
- @RunWith(SpringRunner.class)와 같이 써야함.
- webEnvironment 속성값은 기본적으로 Mock으로 잡혀있다.

## MockMvc
- 서블릿 컨테이너를 테스트용으로 띄우지 않고 Mocking함으로써 DispatcherServlet와 비슷한 효과를 가질 수 있다. 다만 MockMvc라는 클라이언트를 사용해야함
- MockMvc를 만드는 방법은 @AutoConfigureMockMvc 추가하여 @Autowired로 주입받는 것이 가장 쉽다.
- 만약 @SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)을 사용하게 되면 이때부터 내장톰켓을 사용하게 되고 MockMvc를 사용하는 것이 아닌 TestRestTemplate, WebClient를 사용해야함. 
- ex) TestRestTemplate는 바로 빈 주입받을 수 있고 String result = testRestTemplate.getForObject("/", String.class);
assertThat(result).isEqualTo("hello yuseok"); 의 형식으로 사용 가능 
  -> NONE일 경우 서블릿 환경 제공 안 함

## @MockBean 
- ApplicationContext에 들어있는 빈을 Mock으로 만든 객체로 교체 함. 예로 Controller를 거치지 않고 Service 클래스만 테스트 가능 

## WebTestClient
- RestClient 중 하나이며 WebFlux에 특화된 기능
- 기존 RestClient는 Sync이기 때문에 요청을 보내면 끝날때까지 기다려야했다. 반면에 WebTestClient는 Async로 요청을 보내면 기다리지 않고 다른 작업을 할 수 있고, 요청이 끝나면 콜백이 온다. 콜백을 구현하면 됨. 
- WebFlux 의존성이 있어야함. 
- ex) webTestClient.get().uri("/hello").exchange()
          .expectStatus().isOk()
  	  .expectBody(String.class).isEqualTo("hello kim");

## 테스트 빌드가 어떻게 빈을 등록?
- @SpringBootTest가 @SpringBootApplication이 붙은 클래스를 찾고, 그 곳부터 모든 빈 스캔을 한다. 테스트용 애플리케이션 컨텍스트를 만들면서 스캔한 빈을 모두 등록.
- 이후 @MockBean이 있다면 그것만 Mock으로 교체하고 끝. 
- 만약 모든 빈을 등록하는 것이 싫고 테스트에 필요한 것만 등록하고 싶을땐 -> 슬라이스 테스트

## 슬라이스 테스트
- 레이어별로 짤라서 적용 
- @JsonTest: @SpringBootTest 대신 @JsonTest를 사용하여 JacksonTester<>를 주입받아 사용
- @WebMvcTest(컨트롤러.class): 컨트롤러 하나만 테스트 가능, 서비스, 레파지토리 등의 클래스가 빈으로 등록되지 않았기 때문에 @MockBean으로 채워 넣어야 한다.  또한 이는 MockMvc로만 테스트 해야한다. 
- @DataJpaTest: 레파지토리만 테스트, 레파지토리만 빈으로 등록된다.

## 테스트 유틸
- OutputCapture: 제일 많이 사용, Junit의 Rule을 확장한 것
- ex) @Rule
      public OutputCapture outputCapture = new OutputCapture();
      assertThat(outputCapture.toString()).contains("로그 메시지");

- 로그를 비롯해서 콘솔에 찍히는 모든 것을 캡쳐
- 활용: 테스트하고 싶은 곳에 로그를 찍어놓고, 이 로고가 콘솔에 출력  테스트 가능 
- TestPropertyValues
- TestRestTemplate
- ConfigFileApplicationContextInitializer
