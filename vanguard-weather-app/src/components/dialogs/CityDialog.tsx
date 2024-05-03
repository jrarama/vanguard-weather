import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, TextField } from '@mui/material';

type CityProps = {
    open: boolean,
    handleClose: any,
    handleSubmit: any,
    city: string,
    maxTemp: number
}

const CityDialog = ({open, handleClose, handleSubmit, city, maxTemp}: CityProps) => {
    return (
        <Dialog
        open={open}
        onClose={handleClose}
        PaperProps={{
          component: 'form',
          onSubmit: (event: React.FormEvent<HTMLFormElement>) => {
            event.preventDefault();
            const formData = new FormData(event.currentTarget);
            const formJson = Object.fromEntries((formData as any).entries());
            handleSubmit(formJson.city, formJson.max_temp);
          },
        }}
      >
        <DialogTitle>Set Your City</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter your city location:
          </DialogContentText>
          <TextField
            autoFocus
            required
            margin="dense"
            id="city"
            name="city"
            label="City"
            type="input"
            fullWidth
            variant="standard"
            defaultValue={city}
          />
          <DialogContentText>
            Please enter your preferred max temperature:
          </DialogContentText>
          <TextField
            autoFocus
            required
            margin="dense"
            id="max_temp"
            name="max_temp"
            label="Max Temperature"
            type="number"
            fullWidth
            variant="standard"
            defaultValue={maxTemp}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button type="submit">Submit</Button>
        </DialogActions>
      </Dialog>
    );
}

export default CityDialog;