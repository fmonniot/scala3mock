const React = require('react');


const CompLibrary = require('../../core/CompLibrary.js');

const MarkdownBlock = CompLibrary.MarkdownBlock; /* Used to read markdown */
const Container = CompLibrary.Container;
const GridBlock = CompLibrary.GridBlock;

class Index extends React.Component {
    render() {
        const { config: siteConfig, language = '' } = this.props;
        const { baseUrl, docsUrl } = siteConfig;
        const docsPart = `${docsUrl ? `${docsUrl}/` : ''}`;
        const langPart = `${language ? `${language}/` : ''}`;
        const docUrl = doc => `${baseUrl}${docsPart}${langPart}${doc}`;

        const SplashContainer = props => (
            <div className="homeContainer">
                <div className="homeSplashFade">
                    <div className="wrapper homeWrapper">{props.children}</div>
                </div>
            </div>
        );

        const ProjectTitle = props => (
            <h2 className="projectTitle">
                {props.title}
                <small>{props.tagline}</small>
            </h2>
        );

        const PromoSection = props => (
            <div className="section promoSection">
                <div className="promoRow">
                    <div className="pluginRowBlock">{props.children}</div>
                </div>
            </div>
        );

        const Button = props => (
            <div className="pluginWrapper buttonWrapper">
                <a className="button" href={props.href} target={props.target}>
                    {props.children}
                </a>
            </div>
        );
        
        const Block = props => (
            <Container
                padding={['bottom', 'top']}
                id={props.id}
                background={props.background}>
                <GridBlock
                    align={props.align}
                    contents={props.children}
                    layout={props.layout}
                />
            </Container>
        );

        // TODO Change the format slightly to include some pictures, maybe ?
        // scalamock has some generic icons, but they do the job relatively well.
        const hookContent = [
            "**Simple yet powerful**: Scala3Mock has very clean and concise syntax, reasonable defaults, powerful features and is fully type-safe.",
            "**[TBD] Full Scala support**: Full support for Scala 3 features¹ such as: Polymorphic methods, Operators, Overloaded methods, Type constraints, and more.",
            "**[TBD] Popular Test framework integration**: Scala3Mock can be easily used in ScalaTest and Munit testing frameworks.",
            "**[TBD] Quick Start**: Quick introduction to Scala3Mock describing basic usage of this mocking framework",
            "**[TBD] User Guide**: Complete Scala3Mock manual with lots of hints and examples",
            "**[TBD] Open Source**: Scala3Mock is open source and licenced under the MIT licence",
            "\n\n\n¹ Except for feature without compiler support, like implicit/given parameters"
        ];

        const Hook = () => (
            <Block background="" align="left">
                {[
                    {
                        title: '',
                        content: hookContent.join("\n\n"),
                        //image: `${baseUrl}img/hello-printing.png`,
                        imageAlign: 'right',
                    }
                ]}
            </Block>
        );

        return (
            <div>
                <SplashContainer>
                    <div className="inner">
                        <ProjectTitle tagline={siteConfig.tagline} title={siteConfig.title} />
                        <PromoSection>
                            <Button href={docUrl('getting-started')}>Get Started</Button>
                        </PromoSection>
                    </div>
                </SplashContainer>
                <div className="mainContainer" style={{paddingBottom: '0'}}>
                    <Block background="light">
                        {[{
                            title: 'Simple syntax',
                            content: `\`\`\`scala
withExpectations() {
    // Create mock Turtle object
    val mockedTurtle = mock[Turtle]
   
    // Set expectations
    (mockedTurtle.setPosition).expects(10.0, 10.0)
    (mockedTurtle.forward).expects(5.0)
    (mockedTurtle.getPosition).expects().returning(15.0, 10.0)
   
    // Exercise System Under Test
    drawLine(mockedTurtle, (10.0, 10.0), (15.0, 10.0))
}
\`\`\``
                        }]}
                    </Block>
                    <Hook />
                </div>
            </div>
        );
    }
}

module.exports = Index;