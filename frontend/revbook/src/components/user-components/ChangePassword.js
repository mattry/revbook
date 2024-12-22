const ChangePassword = ({currPassword, newPassword, passwordSubmitHandler, setCurrPassword, setNewPassword}) => {

    return (
        <>
        <form onSubmit={passwordSubmitHandler}>
            <label htmlFor="currPassword">Enter your current password: </label>
            <input
                name="currPassword"
                type="password"
                value={currPassword}
                onChange={(e) => setCurrPassword(e.target.value)}
                required 
            /><br/>
            <label htmlFor="newPassword">Enter your new password: </label>
            <input 
                name="newPassword"
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
            /><br/><br/>
            <button>Submit Changes</button><br/>
        </form>
        </>
    );

}

export default ChangePassword;