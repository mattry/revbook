const ChangeName = ({firstName, lastName, nameSubmitHandler, setFirstName, setLastName}) => {

    return(
        <>
        <form onSubmit={nameSubmitHandler}>
            <label htmlFor="firstName">First Name: </label>
            <input
                name="firstName"
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)} 
                required
            />
            <br/>
            <label htmlFor="lastName">Last Name: </label>
            <input 
                name="lastName"
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                required
            /><br/><br/>
            <button>Submit Changes</button><br/>
        </form>
        </>
    );

}

export default ChangeName;