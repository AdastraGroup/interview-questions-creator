var Interview = React.createClass({

getInitialState: function() {
	return {interview: {}, name: "", description: "", evaluationDescription: ""};
},
load: function() {
	$.ajax({    url: "/api/interviews/" + this.props.id, dataType: 'json',
	            success: function(interview) {
	                this.setState({interview: interview, name: interview.name, description: interview.description, evaluationDescription: interview.evaluationDescription});

	                 } .bind(this),
	            error: function(xhr, status, err) { console.error("/api/interviews/" + this.props.id, status, err.toString());}.bind(this)
	});
},
update: function(key, data) {

	$.ajax({ url: "/api/interviews/" + this.props.id, data: JSON.stringify(data), dataType: 'json', type: 'PATCH', headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
		     success: function(result)           { this.onUpdateSuccess(key); }.bind(this),
		     error:   function(xhr, status, err) { this.onUpdateFailure(key, "/api/interviews/" + this.props.id, status, err); }.bind(this)
	});
},
componentDidMount: function() {
    this.load();
},
onChange: function(key, event) {

    var a = {};
    a[key] = event.target.value;

    this.setState( a );
},
onBlur: function(key) {

    if(this.state[key] == this.state.interview[key]) return;

    var a = {};
    a[key] = this.state[key].trim()


    if(a[key] == this.state.interview[key]){
        this.setState( a );
        return;
    }

    this.update(key, a);
},
onUpdateSuccess: function(key) {

    var a = {};
    a[key] = this.state[key].trim();

    this.setState(a);
    this.state.interview[key] = a[key];

},
onUpdateFailure: function(key, url, status, err) {
    console.error(url, status, err.toString());

    var a = {};
    a[key] = this.state.interview[key].trim();
    this.setState(a);

},
render: function() {

    return (
    <div>
        <Input value={this.state.name} onChange={this.onChange.bind(null, "name")} onBlur={this.onBlur.bind(null, "name")} hasFeedback placeholder='Write interview name' label='Interview name:' type='text' />
        <Input value={this.state.description} onChange={this.onChange.bind(null, 'description')} onBlur={this.onBlur.bind(null, "description")} rows="5" label='Interview description:' placeholder='Write interview description' type='textarea'/>
        <Input value={this.state.evaluationDescription} onChange={this.onChange.bind(null, 'evaluationDescription')} onBlur={this.onBlur.bind(null, "evaluationDescription")} rows="5" label='Interview instructions:' placeholder='Write interview instructions for candidate' type='textarea'/>

        <Button bsStyle='primary'>Add question</Button>

    </div>
    );
}
});