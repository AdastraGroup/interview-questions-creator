var Interviews = React.createClass({

getInitialState: function() {
	return {interviews: []};
},
load: function() {
	$.ajax({ url: "/api/interviews", dataType: 'json',
	success: function(value) {
		this.setState({interviews: value._embedded.interviews});
	}.bind(this),
		error: function(xhr, status, err) {
		console.error("/api/interviews", status, err.toString());
	}.bind(this)
	});
},
componentDidMount: function() {
    this.load();
},
render: function() {

    var interviews = this.state.interviews.map(function(interview){
        return <div key={interview.id}>
               <h1>{interview.name}</h1>
               <h4>{interview.description}</h4>
               <div>{interview.evaluationDescription}</div>
               <br/>
               Questions:
               <br/><br/>
               <Questions questions={interview._embedded.questions} />
               </div>
        ;
    });
    return (
        <div className="interviews">
            {interviews}
        </div>
    );
}
});





var Questions = React.createClass({

getInitialState: function() {
	return {questions: this.props.questions};
},

render: function() {

     var questions = this.state.questions.map(function(question){
            return(
                <fieldset key={question.id}>
                    {question.text}:
                    <br/>
                    <Answers answers={question.answers} questionType={question.questionType} />
                </fieldset>
            );
     });

    return <div>{questions}</div>;
}

});


var Answers = React.createClass({

getInitialState: function() {
	return {answers: this.props.answers};
},

render: function() {

var that = this;




    if( this.props.questionType == 'TEXT_AREA'){
        return (
            <textarea key={this.props.answers[0].id} defaultValue={this.props.answers[0].text} rows="4" cols="50"/>
        );
    }

    var answers = this.props.answers.map(function (answer) {
        return ( <input key={answer.id} type={that.props.questionType} name="neco" >{answer.text}</input>);
    });
    return <div className={this.props.questionType}>{answers}</div>

}

});